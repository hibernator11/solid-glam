package com.example.solid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inrupt.client.Headers;
import com.inrupt.client.solid.SolidRDFSource;
import com.inrupt.rdf.wrapping.commons.RDFFactory;
import com.inrupt.rdf.wrapping.commons.TermMappings;
import com.inrupt.rdf.wrapping.commons.ValueMappings;
import com.inrupt.rdf.wrapping.commons.WrapperIRI;
import org.apache.commons.rdf.api.Dataset;
import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDFTerm;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
/**
 * Part 1
 * Note: extends SolidRDFSource
 * To model the Query class as an RDF resource, the Query class extends SolidRDFSource.
 * <p>
 * The @JsonIgnoreProperties annotation is added to ignore non-class-member fields
 * when serializing Query data as JSON.
 */
@JsonIgnoreProperties(value = { "metadata", "headers", "graph", "graphNames", "entity", "contentType" })
public class Query extends SolidRDFSource {
    /**
     * Note 2a: Predicate Definitions
     * The following constants define the Predicates used in our triple statements.
     */
    static IRI RDF_TYPE = rdf.createIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    static IRI SCHEMA_PROVIDER = rdf.createIRI("https://schema.org/provider");
    static IRI SCHEMA_DESCRIPTION = rdf.createIRI("https://schema.org/description");
    static IRI SCHEMA_QUERY = rdf.createIRI("https://schema.org/query");
    static IRI SCHEMA_CATEGORY = rdf.createIRI("https://schema.org/category");
    static IRI SCHEMA_TARGET = rdf.createIRI("https://schema.org/target");

    /**
     * Note 2b: Value Definition
     * The following constant define the value for the predicate RDF_TYPE.
     */
    static URI MY_RDF_TYPE_VALUE = URI.create("https://schema.org/SearchAction");
    /**
     * Note 3: Node class
     * The Node class is an inner class (defined below) that handles the mapping between expense data and RDF triples.
     * The subject contains the expense data.
     */
    private final Node subject;
    /**
     * Note 4: Constructors
     * Expense constructors to handle SolidResource fields:
     * - identifier: The destination URI of the resource; e.g., https://myPod.example.com/myPod/search1
     * - dataset: The org.apache.commons.rdf.api.Dataset that corresponding to the resource.
     * - headers:  The com.inrupt.client.Headers that contains HTTP header information.
     * <p>
     * In addition, the subject field is initialized.
     */
    public Query(final URI identifier, final Dataset dataset, final Headers headers) {
        super(identifier, dataset, headers);
        this.subject = new Node(rdf.createIRI(identifier.toString()), getGraph());
    }
    public Query(final URI identifier) {
        this(identifier, null, null);
    }
    @JsonCreator
    public Query(@JsonProperty("identifier") final URI identifier,
                   @JsonProperty("provider") String provider,
                   @JsonProperty("description") String description,
                   @JsonProperty("query") String query,
                   @JsonProperty("category") String category,
                   @JsonProperty("target") String target) {
        this(identifier);
        this.setRDFType(MY_RDF_TYPE_VALUE);
        this.setProvider(provider);
        this.setDescription(description);
        this.setQuery(query);
        this.setCategory(category);
        this.setTarget(target);
    }
    /**
     * Note 5: Various getters/setters.
     * The getters and setters reference the subject's methods.
     */
    public URI getRDFType() {
        return subject.getRDFType();
    }
    public void setRDFType(URI rdfType) {
        subject.setRDFType(rdfType);
    }
    public String getProvider() {
        return subject.getProvider();
    }
    public void setProvider(String provider) {
        subject.setProvider(provider);
    }
    public void setQuery(String query) {
        subject.setQuery(query);
    }
    public String getDescription() {
        return subject.getDescription();
    }
    public void setDescription(String description) {
        subject.setDescription(description);
    }
    public String getTarget() {
        return subject.getTarget();
    }
    public void setTarget(String target) {
        subject.setTarget(target);
    }
    public String getCategory() {
        return subject.getCategory();
    }
    public void setCategory(String category) {
        subject.setCategory(category);
    }
    /**
     * Note 6: Inner class ``Node`` that extends WrapperIRI
     * Node class handles the mapping of the expense data (date, provider,
     * description, category, priceCurrency, total) to RDF triples
     * <subject> <predicate> <object>.
     * <p>
     * Nomenclature Background: A set of RDF triples is called a Graph.
     */
    class Node extends WrapperIRI {
        Node(final RDFTerm original, final Graph graph) {
            super(original, graph);
        }
        URI getRDFType() {
            return anyOrNull(RDF_TYPE, ValueMappings::iriAsUri);
        }
        /**
         * Note 7: In its getters, the ``Node`` class calls WrapperBlankNodeOrIRI
         * method ``anyOrNull`` to return either 0 or 1 value mapped to the predicate.
         * You can use ValueMappings method to convert the value to a specified type.
         * <p>
         * In its setters, the ``Node`` class calls WrapperBlankNodeOrIRI
         * method ``overwriteNullable`` to return either 0 or 1 value mapped to the predicate.
         * You can use TermMappings method to store the value with the specified type information.
         */
        void setRDFType(URI type) {
            overwriteNullable(RDF_TYPE, type, TermMappings::asIri);
        }
        String getProvider() {
            return anyOrNull(SCHEMA_PROVIDER, ValueMappings::literalAsString);
        }
        void setProvider(String provider) {
            overwriteNullable(SCHEMA_PROVIDER, provider, TermMappings::asStringLiteral);
        }
        String getQuery() {
            return anyOrNull(SCHEMA_QUERY, ValueMappings::literalAsString);
        }
        void setQuery(String query) {
            overwriteNullable(SCHEMA_QUERY, query, TermMappings::asStringLiteral);
        }

        String getDescription() {
            return anyOrNull(SCHEMA_DESCRIPTION, ValueMappings::literalAsString);
        }
        void setDescription(String description) {
            overwriteNullable(SCHEMA_DESCRIPTION, description, TermMappings::asStringLiteral);
        }

        String getTarget() {
            return anyOrNull(SCHEMA_TARGET, ValueMappings::literalAsString);
        }
        void setTarget(String target) {
            overwriteNullable(SCHEMA_TARGET, target, TermMappings::asStringLiteral);
        }

        public String getCategory() {
            return anyOrNull(SCHEMA_CATEGORY, ValueMappings::literalAsString);
        }
        public void setCategory(String category) {
            overwriteNullable(SCHEMA_CATEGORY, category, TermMappings::asStringLiteral);
        }
    }
}