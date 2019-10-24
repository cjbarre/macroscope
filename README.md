# macroscope

`docker-compose up -d`

Connect to the storage database using the values in the .env file for now.

Query things in the warehouse schema.

Tables are prefixed by an abbreviation of the data source name.

- ees = establishment employment situation
- hes = household employment situation
- jolts = job openings and labor turnover survey

Known issues:

Start fresh if you run the init container twice, it should only ever run once.

Supported Data Sources:

- Employment Situation Report https://www.bls.gov/news.release/empsit.toc.htm
  - Household
  - Establishment
- Job Openings and Labor Turnover Survey https://www.bls.gov/jlt/