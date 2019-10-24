# macroscope

`docker-compose up -d`

Connect to the storage database using the values in the .env file for now, query things in the following schemas.

- establishment_employment_situation

Known issues:

Start fresh if you run the init container twice, it should only ever run once.

Supported Data Sources:

- Employment Situation Report https://www.bls.gov/news.release/empsit.toc.htm
  - Household
  - Establishment
- Job Openings and Labor Turnover Survey https://www.bls.gov/jlt/