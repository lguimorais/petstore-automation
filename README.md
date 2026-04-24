# Petstore API Automation

Automação de testes da API Swagger Petstore usando Postman + Newman + GitHub Actions.

## Base URL
https://petstore.swagger.io/v2

## Como rodar
```bash
newman run collections/Petstore_v2.postman_collection.json \
  -e environments/Petstore_Env.postman_environment.json
```
