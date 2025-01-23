db = db.getSiblingDB("o2o"); // Replace with your database name
db.createCollection("stores");
db.stores.createIndex({ location: "2dsphere" });
