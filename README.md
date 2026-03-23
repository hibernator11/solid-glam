# solid-GLAM
The project presents Spring Boot personal (i.e., single-user) Web service on http://localhost:8080 that uses Inrupt’s Java client library to:
- Read the Pod URLs associated with the user’s WebID.
- Store and manage SPARQL query records in the user’s Pod (Personal Online Datastore). The query records are stored as Resource Description Framework (RDF) resource.

## Examples of use
```
curl -X GET http://localhost:8080/api/all-pods\?webid\=SUBSTITUTE_YOUR_WEBID
```

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


## References
- https://docs.inrupt.com/sdk/java-sdk/tutorial
