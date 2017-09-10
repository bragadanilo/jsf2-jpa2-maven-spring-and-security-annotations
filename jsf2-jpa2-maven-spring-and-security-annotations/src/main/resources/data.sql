MERGE INTO "users" USING (VALUES('admin','$2a$10$qycerNEnmJ6ajED27ESu8uC44DMaYdys77p4dEUT3IzCMMKOa70hm', true)) 
   AS vals(x,y,z) ON "users"."username" = vals.x
   WHEN MATCHED THEN UPDATE SET "users"."active" = true -- do nothing
   WHEN NOT MATCHED THEN INSERT ("username", "password", "active") VALUES (vals.x, vals.y, vals.z);
   
MERGE INTO "user_roles" USING (VALUES('ROLE_ADMIN', 1)) 
   AS vals(x,y) ON "user_roles"."role" = vals.x
   WHEN MATCHED THEN UPDATE SET "user_roles"."role" = vals.x -- do nothing
   WHEN NOT MATCHED THEN INSERT ("role", "user_id") VALUES (vals.x, vals.y);