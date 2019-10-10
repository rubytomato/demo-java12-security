DROP TABLE IF EXISTS user;
CREATE TABLE user (
  id BIGINT AUTO_INCREMENT                    COMMENT 'ユーザーID',
  name VARCHAR(60) NOT NULL                   COMMENT 'ユーザー名',
  email VARCHAR(120) NOT NULL                 COMMENT 'メールアドレス',
  password VARCHAR(255) NOT NULL              COMMENT 'パスワード',
  roles VARCHAR(120)                          COMMENT 'ロール',
  lock_flag BOOLEAN NOT NULL DEFAULT 0        COMMENT 'ロックフラグ 1:ロック',
  disable_flag BOOLEAN NOT NULL DEFAULT 0     COMMENT '無効フラグ 1:無効',
  create_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  update_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id)
)
ENGINE = INNODB
DEFAULT CHARSET = UTF8MB4
COMMENT = 'ユーザーテーブル';

ALTER TABLE user ADD CONSTRAINT UNIQUE KEY UKEY_user_email (email);


DROP TABLE IF EXISTS user_profile;
CREATE TABLE user_profile (
  id BIGINT AUTO_INCREMENT                    COMMENT 'ユーザープロフィールID',
  user_id BIGINT NOT NULL                     COMMENT 'ユーザーID',
  nick_name VARCHAR(60)                       COMMENT 'ニックネーム',
  avatar_image MEDIUMBLOB                     COMMENT 'アバターイメージ',
  create_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  update_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id)
)
ENGINE = INNODB
DEFAULT CHARSET = UTF8MB4
COMMENT = 'ユーザープロフィールテーブル';

ALTER TABLE user_profile ADD CONSTRAINT FOREIGN KEY FKEY_user_profile_id_user_id (user_id) REFERENCES user (id);
