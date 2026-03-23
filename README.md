# Solid-GLAM
The project presents Spring Boot personal (i.e., single-user) Web service on http://localhost:8080 that uses Inrupt’s Java client library to:
- Read the Pod URLs associated with the user’s WebID.
- Store and manage SPARQL query records in the user’s Pod (Personal Online Datastore). The query records are stored as Resource Description Framework (RDF) resource.

This example is based on the tutorial https://docs.inrupt.com/sdk/java-sdk/tutorial.

[Solid](https://solidproject.org/) is an evolution of the web by its creator Tim Berners-Lee realizing his original vision for the Web. User can store information once and decide who can access what, when you need services like mortgage applications or medical care. As artificial intelligence becomes central to digital services, Solid ensures people maintain control over their data while benefiting from intelligent applications.

## Prerequisites

To get a WebID and a Pod on PodSpaces:

- Go to [PodSpaces](https://start.inrupt.com/).
- The application displays your WebID and Pod Storage details:
- WebID: ```https://id.inrupt.com/{username}``` 
- Pod Storage: ```https://storage.inrupt.com/{Root Container}```

Inrupt’s PodSpaces provides an [Application Registration page](https://login.inrupt.com/registration.html) where you can statically register your applications to generate credentials for them.
- Go to [PodSpaces Application Registration](https://login.inrupt.com/registration.html) page.
- If not already logged in, you will redirect to the login page. Log in with your username and password.
- In the Register an app textbox, enter your application’s name and click Register.
- The Client ID and Client Secret for your application appears under Apps You’ve Registered list.

## Run your server and Enter Your Client Credentials

Export your registered client credentials (see the Prerequisites) as environment variables.

1. Identity Provider (the IDP with whom you registered your application):

```read -s MY_SOLID_IDP && export MY_SOLID_IDP```

Enter https://login.inrupt.com

2. Client ID:

```read -s MY_SOLID_CLIENT_ID && export MY_SOLID_CLIENT_ID```

Enter your Client ID.

3. Client Secret:

```read -s MY_SOLID_CLIENT_SECRET && export MY_SOLID_CLIENT_SECRET```

Enter your Client Secret.

4. Authentication Flow Method:

```read -s MY_AUTH_FLOW && export MY_AUTH_FLOW```

Enter ```client_secret_basic```

## Examples of use

Getting our list of pods:
```
curl -X GET http://localhost:8080/api/all-pods\?webid\=SUBSTITUTE_YOUR_WEBID
```

Creating a query record to store a [federated SPARQL query to retrieve all the works from the National Library of France of the author Ramón y Cajal](https://w.wiki/KDm9):

```
curl -X POST http://localhost:8080/api/query/create \
   -H 'Content-type:application/json'  \
   -d '{
      "identifier": "https://storage.inrupt.com/your-root-container/query/20260323/search2",
      "provider": "Wikidata",
      "description": "Example of SPARQL query",
      "target": "National Library of France",
      "query": "PREFIX dcterms: <http:\/\/purl.org\/dc\/terms\/>\r\nPREFIX rdarelationships: <http:\/\/rdvocab.info\/RDARelationshipsWEMI\/>\r\nPREFIX rdagroup1elements: <http:\/\/rdvocab.info\/Elements\/>\r\n\r\nSELECT ?author ?expression ?title ?edition ?placeOfPublication ?yearOfPublication ?langCode WHERE {\r\nwd:Q150526 wdt:P268 ?id\r\nBIND(uri(concat(concat(\"http:\/\/data.bnf.fr\/ark:\/12148\/cb\", ?id),\"#about\")) as ?author)\r\nSERVICE <http:\/\/data.bnf.fr\/sparql> {\r\n  ?expression <http:\/\/id.loc.gov\/vocabulary\/relators\/aut> ?author .\r\n  OPTIONAL {?expression dcterms:language ?langCode .}\r\n  OPTIONAL {?manifestation dcterms:publisher ?edition .}\r\n  ?manifestation rdarelationships:expressionManifested ?expression .\r\n  ?manifestation dcterms:title ?title .\r\n  ?manifestation dcterms:date ?yearOfPublication .\r\n  OPTIONAL{ ?manifestation rdagroup1elements:placeOfPublication ?placeOfPublication .}\r\n  }\r\n  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],mul,en\". }\r\n\r\n}\r\nlimit 100",
      "category": "Creative works and authors" }'
```

Retrieving a query record:

```
curl -X GET http://localhost:8080/api/query/get\?resourceURL\=https://storage.inrupt.com/20052ae5-630f-41f1-9f0a-595283170307/expenses/20260323/search1
```

## Licence
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Licence Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/80x15.png" /></a><br />Content is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International license</a>.

Please, note that the datasets used in this project have separate licences.

## References
- https://docs.inrupt.com/sdk/java-sdk/tutorial
- https://solidproject.org/
