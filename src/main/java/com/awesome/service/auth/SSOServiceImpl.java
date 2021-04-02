package com.awesome.service.auth;

import com.awesome.controller.dto.UserCreateDTO;
import com.awesome.controller.dto.UserInsertDTO;
import com.awesome.exception.AppException;
import org.dozer.Mapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@Component
public class SSOServiceImpl implements SingleSignOnService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String SERVER_URL;
    private final String REALM;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private static final String GRANT_TYPE = "client_credentials";
    private final Keycloak keycloakBuilder;
    private final RealmResource realmResource;
    private final UsersResource userResource;

    @Autowired
    @Lazy
    private Mapper dozerMapper;

    public SSOServiceImpl(@Value("${keycloak.auth-server-url}") String server_url,
                          @Value("${keycloak.realm}") String realm,
                          @Value("${keycloak.resource}") String client_id,
                          @Value("${keycloak.credentials.secret}") String client_secret) {

        this.SERVER_URL = server_url;
        this.REALM = realm;
        this.CLIENT_ID = client_id;
        this.CLIENT_SECRET = client_secret;

        this.keycloakBuilder = KeycloakBuilder
                .builder()
                .serverUrl(SERVER_URL)
                .realm(REALM)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .grantType(GRANT_TYPE)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        this.realmResource = keycloakBuilder.realm(REALM);
        this.userResource = realmResource.users();
    }

    @Override
    public String retrieveToken(){
        return this.keycloakBuilder.tokenManager().getAccessTokenString();
    }

    @Override
    public String getUsers() {
        String accessToken = retrieveToken();

        ResteasyClient client = new ResteasyClientBuilder().build();
        String endpoint = String.format("%s/admin/realms/%s/%s", this.SERVER_URL, this.REALM, "users");

        logger.info(accessToken);
        logger.info(endpoint);

        ResteasyWebTarget target = client.target(endpoint);

        Response response = target.request().header("Authorization", "Bearer " + accessToken).get();
        String value = response.readEntity(String.class);
        response.close();  // You should close connections!
        return value;
    }

    private String createKeycloakUser(UserCreateDTO userCreateDTO) {
        // set user information
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userCreateDTO.getEmail());
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setEmail(userCreateDTO.getEmail());

        // Create user in keycloak (requires manage-users role)
        Response response = this.userResource.create(user);

        //handle status response.
        var status = response.getStatus();
        if (status != HttpStatus.CREATED.value() || response.getLocation() == null) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "EX60003", Arrays.asList("Insert user failed."));
        }

        //get user id
        String userPath = response.getLocation().getPath();
        String userId = userPath.substring(userPath.lastIndexOf("/") + 1);

        return userId;
    }

    private void assignKeycloakRoleForUser(UserInsertDTO userInsertDTO) {
        // Get all realm role,then filter member role, work around due to get role by name failed (I think this is a bug from keycloak).
        // todo: [hungpham] need open issue to repo keycloak.
        var roleOpt = this.realmResource.roles().list().stream().filter(c -> c.getName().equals("member")).findFirst();

        if (roleOpt.isEmpty()) throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "EX50005", Arrays.asList());
        var role = roleOpt.get();

        // Assign realm role member to user
        this.userResource.get(userInsertDTO.getUserKeyCloakId()).roles().realmLevel().add(Arrays.asList(role));
    }

    private void updateKeycloakPassword(UserInsertDTO userInsertDTO) {
        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userInsertDTO.getPassword());

        // Set password credential
        this.userResource.get(userInsertDTO.getUserKeyCloakId()).resetPassword(passwordCred);
    }

    @Override
    public void handleInsertUserFailed(String userKeycloakId) {
        //insert user failed, so delete user in keycloak as well.
        this.userResource.delete(userKeycloakId);
    }

    @Override
    public UserInsertDTO registerUser(UserCreateDTO userCreateDTO) throws Exception {
        //insert keycloak user.
        var keycloakId = createKeycloakUser(userCreateDTO);

        var userInsert = dozerMapper.map(userCreateDTO, UserInsertDTO.class);
        userInsert.setUserKeyCloakId(keycloakId);

        //assign keycloak role to user.
        assignKeycloakRoleForUser(userInsert);

        //set keycloak user's pass.
        updateKeycloakPassword(userInsert);

        return userInsert;
    }
}