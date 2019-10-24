10-23-2019

The project is minimally functional. It's currently structured as a docker compose application with a postgres container for data storage and an init container. 

The initialization procedure imports data from various sources on the first run of the software. The init container should only execute once for a fresh install. The likelihood of a second execution is high, being part of a docker compose application. It will be made idempotent shortly. 

Scripts used in the init container are in the bin directory.

Configuration for data sources is in the configuration directory.

Building for the first time takes a while because of an ubuntu image, planning to switch it over to alpine later.

Current environmental configuration:

These are used by the storage container to setup postgres and by the init container to access the database. 

They can be found in the .env file.

STORAGE_PASSWORD
STORAGE_USER
STORAGE_DB
STORAGE_HOST

The first supported data source is the establishment survery portion of the monthly employment situation report released on the first Friday of every month. 

BLS data sets are typically structured similarly, if not the exact same. The configuration file for BLS data is a CSV file where the key is a table name and the value is a URL.

datatype,https://download.bls.gov/pub/time.series/ce/ce.datatype

The bls-loader script in the bin directory accepts this format as its input and carries out the ETL process against every table in the file in parallel.

The init script is used to string together the processing of the independent data sources.

Once it's running, connect to the storage database using the information in the .env file for now and fire off a query:

select * from establishment_employment_situation.series order by series_title;

Added support for the household portion of the employment situation report and the JOLT survey.

Added Postgrest to the environment. Data sources are now tables inside one schema called warehouse. Data sources are prefixes on table names.