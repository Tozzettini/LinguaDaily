To run API (including the postgres database) only for the first start:
1. Ensure that docker is installed and running.
2. Start docker compose and build: ```docker-compose up --build```

To update API without wiping the database:
1. Rebuild only the API container: ```docker-compose build app```
2. Restart only the API container: ```docker-compose up -d --no-deps app```