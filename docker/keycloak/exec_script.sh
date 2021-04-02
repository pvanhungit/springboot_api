#! /bin/sh
exec_path="/opt/jboss/keycloak/bin/kcadm.sh"
docker exec -it keycloak sh
sh $exec_path config credentials --server http://localhost:8080/auth --realm master --user admin --password 3entropy
sh $exec_path create realms -s realm=entropy-data -s enabled=true -o
sh $exec_path create partialImport -r entropy-data -s ifResourceExists=SKIP -o -f '/tmp/import_realm.json'
