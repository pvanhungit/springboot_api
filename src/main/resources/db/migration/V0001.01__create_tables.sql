-- auto update updated_at
create function set_update_time()
  returns trigger as $$
  begin
    new.updated_at = now();
    return new;
  end;
$$ language 'plpgsql';

-- auto update created_at
create function set_create_time()
  returns trigger as $$
  begin
    new.created_at = now();
    return new;
  end;
$$ language 'plpgsql';

CREATE TABLE account (
   user_id serial PRIMARY KEY,
   phone_number varchar(20) unique,
   email varchar(50) unique,
   first_name varchar(50),
   last_name varchar(50),
   user_keycloak_id varchar(500),
   profile_img_url text,
   is_enabled boolean NOT NULL,
   is_subscribing boolean default false,
   reward bigint,
   reward_name_id int,
   created_by bigint not null,
   created_at timestamp default CURRENT_TIMESTAMP not null,
   updated_by bigint,
   updated_at timestamp default CURRENT_TIMESTAMP not null
);

create index account_idx_phone
    on account(phone_number);
create index account_idx_email
    on account(email);

CREATE TABLE category(
  category_id serial primary key,
  name varchar(255) not null,
  created_by bigint not null,
  created_at timestamp default CURRENT_TIMESTAMP not null,
  updated_by bigint,
  updated_at timestamp default CURRENT_TIMESTAMP not null
);

CREATE TABLE post(
  post_id serial PRIMARY KEY,
  author_id bigint references account(user_id),
  title varchar(255) not null,
  img text[],
  content text,
  tags text[],
  category_id bigint references category(category_id),
  up_vote int,
  down_vote int,
  created_at timestamp default CURRENT_TIMESTAMP not null,
  updated_by bigint,
  updated_at timestamp default CURRENT_TIMESTAMP not null
);

create index post_idx_title
  on post(title);
create index post_idx_author
  on post(author_id);

create table comment(
    comment_id serial primary key,
    post_id bigint references post(post_id),
    content bigint not null,
    user_id serial references account(user_id),
    parent_comment_id bigint,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null
);

create table comment_vote(
    comment_id bigint references comment(comment_id),
    user_id bigint references account(user_id)
);

create table reward(
    id serial primary key,
    name varchar(50) not null
);

alter table account
    add constraint fk_reward_name
    foreign key (reward_name_id)
    REFERENCES reward(id);

CREATE TRIGGER insert_trigger
BEFORE INSERT
    ON account
    EXECUTE PROCEDURE set_create_time();

CREATE TRIGGER update_trigger
BEFORE UPDATE
    ON account
    EXECUTE PROCEDURE set_update_time();

CREATE TRIGGER insert_trigger
BEFORE INSERT
    ON post
    EXECUTE PROCEDURE set_create_time();

CREATE TRIGGER update_trigger
BEFORE UPDATE
    ON post
    EXECUTE PROCEDURE set_update_time();

CREATE TRIGGER insert_trigger
BEFORE INSERT
    ON category
    EXECUTE PROCEDURE set_create_time();

CREATE TRIGGER update_trigger
BEFORE UPDATE
    ON category
    EXECUTE PROCEDURE set_update_time();

CREATE TRIGGER insert_trigger
BEFORE INSERT
    ON comment
    EXECUTE PROCEDURE set_create_time();

CREATE TRIGGER update_trigger
BEFORE UPDATE
    ON comment
    EXECUTE PROCEDURE set_update_time();