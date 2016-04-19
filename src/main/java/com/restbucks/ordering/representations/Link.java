package com.restbucks.ordering.representations;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(namespace = Representation.DAP_NAMESPACE)
public class Link {
    
    private static final Logger LOG = LoggerFactory.getLogger(Link.class);
    
    @XmlAttribute(name = "rel")
    private String rel;
    @XmlAttribute(name = "uri")
    private String uri;

    @XmlAttribute(name = "mediaType")
    private String mediaType;

    /**
     * For JAXB :-(
     */
    Link() {
        LOG.debug("Link Constructor");
    }

    public Link(String name, RestbucksUri uri, String mediaType) {
        LOG.info("Creating a Link object");
        LOG.debug("name = {}", name);
        LOG.debug("uri = {}", uri);
        LOG.debug("mediaType = {}", mediaType);
        
        this.rel = name;
        this.uri = uri.getFullUri().toString();
        this.mediaType = mediaType;

        LOG.debug("Created Link Object {}", this);
    }

    public Link(String name, RestbucksUri uri) {
        this(name, uri, Representation.RESTBUCKS_MEDIA_TYPE);
    }

    public String getRelValue() {
        return rel;
    }

    public URI getUri() {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMediaType() {
        return mediaType;
    }
}
