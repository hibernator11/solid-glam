package com.example.solid;

import com.inrupt.client.auth.Session;
import com.inrupt.client.openid.OpenIdSession;
import com.inrupt.client.solid.SolidSyncClient;
import com.inrupt.client.webid.WebIdProfile;
import com.inrupt.client.solid.PreconditionFailedException;
import com.inrupt.client.solid.ForbiddenException;
import com.inrupt.client.solid.NotFoundException;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.rdf.api.RDFSyntax;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Set;
@RequestMapping("/api")
@RestController
public class QueryController {
    /**
     * Note 1: Authenticated Session
     * Using the client credentials, create an authenticated session.
     */
    final Session session = OpenIdSession.ofClientCredentials(
            URI.create(System.getenv("MY_SOLID_IDP")),
            System.getenv("MY_SOLID_CLIENT_ID"),
            System.getenv("MY_SOLID_CLIENT_SECRET"),
            System.getenv("MY_AUTH_FLOW"));
    /**
     * Note 2: SolidSyncClient
     * Instantiates a synchronous client for the authenticated session.
     * The client has methods to perform CRUD operations.
     */
    final SolidSyncClient client = SolidSyncClient.getClient().session(session);
    private final PrintWriter printWriter = new PrintWriter(System.out, true);
    /**
     * Note 3: SolidSyncClient.read()
     * Using the SolidSyncClient client.read() method, reads the user's WebID Profile document and returns the Pod URI(s).
     */
    @GetMapping("/all-pods")
    public Set<URI> getPods(@RequestParam(value = "webid", defaultValue = "") String webID) {
        printWriter.println("QueryController:: getPods");
        try (final var profile = client.read(URI.create(webID), WebIdProfile.class)) {
            return profile.getStorages();
        }
    }
    /**
     * Note 4: SolidSyncClient.create()
     * Using the SolidSyncClient client.create() method,
     * - Saves the Query as an RDF resource to the location specified in the Query.identifier field.
     */
    @PostMapping(path = "/query/create")
    public Query createQuery(@RequestBody Query newQuery) {
        printWriter.println("QueryController:: createQuery");
        try (var createdQuery = client.create(newQuery)) {
            printQueryAsTurtle(createdQuery);
            return createdQuery;
        } catch(PreconditionFailedException e1) {
            // Errors if the resource already exists
            printWriter.println(String.format("[%s] com.inrupt.client.solid.PreconditionFailedException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to create
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Note 5: SolidSyncClient.read()
     * Using the SolidSyncClient client.read() method,
     * - Reads the RDF resource into the Query class.
     */
    @GetMapping("/query/get")
    public Query getQuery(@RequestParam(value = "resourceURL", defaultValue = "") String resourceURL) {
        printWriter.println("QueryController:: getQuery");
        try (var resource = client.read(URI.create(resourceURL), Query.class)) {
            return resource;
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Note 6: SolidSyncClient.update()
     * Using the SolidSyncClient client.update() method,
     * - Updates the Query resource.
     */
    @PutMapping("/query/update")
    public Query updateQuery(@RequestBody Query query) {
        printWriter.println("Query:: updateQuery");
        try(var updatedQuery = client.update(query)) {
            printQueryAsTurtle(updatedQuery);
            return updatedQuery;
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Note 7: SolidSyncClient.delete()
     * Using the SolidSyncClient client.delete() method,
     * - Deletes the resource located at the resourceURL.
     */
    @DeleteMapping("/queries/delete")
    public void deleteQuery(@RequestParam(value = "resourceURL") String resourceURL) {
        printWriter.println("QueryController:: deleteQuery");
        try {
            client.delete(URI.create(resourceURL));
            // Alternatively, you can specify a Query object to the delete method.
            // The delete method deletes  the Query recorde located in the Query.identifier field.
            // For example: client.delete(new Query(URI.create(resourceURL)));
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Note 8: Prints the query resource in Turtle.
     */
    private void printQueryAsTurtle(Query query) {
        printWriter.println("QueryController:: printQueryAsTurtle");
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try  {
            query.serialize(RDFSyntax.TURTLE, content);
            printWriter.println(content.toString("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
