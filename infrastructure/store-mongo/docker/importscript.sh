#!/bin/bash

mongoimport --db='o2o' --collection='stores' --file='/tmp/stores.json' --jsonArray --username='root' --password='root' --authenticationDatabase=admin
