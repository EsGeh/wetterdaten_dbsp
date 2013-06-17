/*
 * MySQL
 */



SET NAMES 'utf8';

BEGIN;
/*
 * Table structure for table 'geodb_type_names'
 */

create table geodb_type_names (
  type_id              integer not null,
  type_locale          varchar(5) not null,
  name                 varchar(255) not null,             /* varchar(500)? */
unique (type_id, type_locale)
) TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_locations'
 */

create table geodb_locations (
  loc_id               integer not null primary key,
  loc_type             integer not null
    check (loc_type = 100100000 or loc_type = 100200000 or
           loc_type = 100300000 or loc_type = 100400000 or
           loc_type = 100500000 or loc_type = 100600000 or
           loc_type = 100700000 or loc_type = 100800000 or
           loc_type = 100900000 or loc_type = 101000000 or 
	   loc_type = 1)
) TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_hierarchies'
 */

create table geodb_hierarchies (
  loc_id               integer not null references geodb_locations,
  level                integer not null check (level>0 and level<=9),
  id_lvl1              integer not null,
  id_lvl2              integer,
  id_lvl3              integer,
  id_lvl4              integer,
  id_lvl5              integer,
  id_lvl6              integer,
  id_lvl7              integer,
  id_lvl8              integer,
  id_lvl9              integer,
  valid_since          date,
  date_type_since      integer,
  valid_until          date not null,
  date_type_until      integer not null
  check (
    (
      (level = 1 and /* loc_id = id_lvl1 and */
                     id_lvl2 is null and id_lvl3 is null and
                     id_lvl4 is null and id_lvl5 is null and
                     id_lvl6 is null and id_lvl7 is null and
                     id_lvl8 is null and id_lvl9 is null) or
      (level = 2 and /* loc_id = id_lvl2 and */
                     id_lvl1 is not null and id_lvl3 is null and
                     id_lvl4 is null and id_lvl5 is null and
                     id_lvl6 is null and id_lvl7 is null and
                     id_lvl8 is null and id_lvl9 is null) or
      (level = 3 and /* loc_id = id_lvl3 and */
                     id_lvl1 is not null and id_lvl2 is not null and
                     id_lvl4 is null and id_lvl5 is null and
                     id_lvl6 is null and id_lvl7 is null and
                     id_lvl8 is null and id_lvl9 is null) or
      (level = 4 and /* loc_id = id_lvl4 and */
                     id_lvl1 is not null and id_lvl2 is not null and
                     id_lvl3 is not null and id_lvl5 is null and
                     id_lvl6 is null and id_lvl7 is null and
                     id_lvl8 is null and id_lvl9 is null) or
      (level = 5 and /* loc_id = id_lvl5 and */
                     id_lvl1 is not null and id_lvl2 is not null and
                     id_lvl3 is not null and id_lvl4 is not null and
                     id_lvl6 is null and id_lvl7 is null and
                     id_lvl8 is null and id_lvl9 is null) or
      (level = 6 and /* loc_id = id_lvl6 and */
                     id_lvl1 is not null and id_lvl2 is not null and
                     id_lvl3 is not null and id_lvl4 is not null and
                     id_lvl5 is not null and id_lvl7 is null and
                     id_lvl8 is null and id_lvl9 is null) or
      (level = 7 and /* loc_id = id_lvl7 and */
                     id_lvl1 is not null and id_lvl2 is not null and
                     id_lvl3 is not null and id_lvl4 is not null and
                     id_lvl5 is not null and id_lvl6 is not null and
                     id_lvl8 is null and id_lvl9 is null) or
      (level = 8 and /* loc_id = id_lvl8 and */
                     id_lvl1 is not null and id_lvl2 is not null and
                     id_lvl3 is not null and id_lvl4 is not null and
                     id_lvl5 is not null and id_lvl6 is not null and
                     id_lvl7 is not null and id_lvl9 is null) or
      (level = 9 and /* loc_id = id_lvl9 and */
                     id_lvl1 is not null and id_lvl2 is not null and
                     id_lvl3 is not null and id_lvl4 is not null and
                     id_lvl5 is not null and id_lvl6 is not null and
                     id_lvl7 is not null and id_lvl8 is not null)
      ) and
      (
        (valid_since is null and date_type_since is null) or
        (valid_since is not null and date_type_since is not null)
      )
  )
) TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_coordinates'
 */

create table geodb_coordinates (
  loc_id               integer not null references geodb_locations,
  coord_type           integer not null check (coord_type=200100000),
  lat                  double precision,
  lon                  double precision,
  coord_subtype        integer,
  valid_since          date,
  date_type_since      integer,
  valid_until          date not null,
  date_type_until      integer not null
) TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_textdata'
 */

create table geodb_textdata (
  loc_id               integer not null references geodb_locations,
  text_type            integer not null,
  text_val             varchar(255) not null,                  /* varchar(2000)? */
  text_locale          varchar(5),                          /* ISO 639-1 */
  is_native_lang       smallint(1),
  is_default_name      smallint(1),
  valid_since          date,
  date_type_since      integer,
  valid_until          date not null,
  date_type_until      integer not null,
    check (
      (
        (
          (text_type = 500100000 or text_type = 500100004 or
           text_type = 500100002 or text_type = 500700000 or
           text_type = 500700001 or text_type = 500800000 or
           text_type = 500800000 or text_type = 500900000
          ) and
          text_locale like '__%' and
          is_native_lang is not null and
          is_default_name is not null
        ) or
        (
          (text_type = 500100001 or text_type = 500100003 or
           text_type = 500300000 or text_type = 500500000 or
           text_type = 500600000
          ) and
          text_locale is null and
          is_native_lang is null and
          is_default_name is null
        )
      ) and
        (
          (valid_since is null and date_type_since is null) or
          (valid_since is not null and date_type_since is not null)
        )
    )
) TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_intdata'
 */

create table geodb_intdata (
  loc_id               integer not null references geodb_locations,
  int_type             integer not null,
  int_val              bigint not null,
  valid_since          date,
  date_type_since      integer,
  valid_until          date not null,
  date_type_until      integer not null
) TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_floatdata'
 */

create table geodb_floatdata (
  loc_id               integer not null references geodb_locations,
  float_type           integer not null,
  float_val            double precision not null,    /* double / float??? */
  valid_since          date,
  date_type_since      integer,
  valid_until          date not null,
  date_type_until      integer not null
) TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_changelog'
 */

create table geodb_changelog (
  id                   integer not null primary key,
  datum                date not null,
  beschreibung         text not null,
  autor                varchar(50) not null,
  version              varchar(8)
) TYPE=InnoDB CHARACTER SET utf8;
COMMIT;
