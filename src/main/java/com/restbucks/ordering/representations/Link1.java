package com.restbucks.ordering.representations;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(namespace = Representation1.DAP_NAMESPACE)
public class Link1 {
    
    private static final Logger LOG = LoggerFactory.getLogger(Link1.class);
    
    @XmlAttribute(name = "rel")
    private String rel;
    @XmlAttribute(name = "uri")
    private String uri;

    @XmlAttribute(name = "mediaType")
    private String mediaType;

    /**
     * For JAXB :-(
     */
    Link1() {
        LOG.info("Link Constructor");
    }

    public Link1(String name, AppealsUri uri, String mediaType) {
        LOG.info("Creating a Link object");
        LOG.debug("name = {}", name);
        LOG.debug("uri = {}", uri);
        LOG.debug("mediaType = {}", mediaType);
        
        this.rel = name;
        this.uri = uri.getFullUri().toString();
        this.mediaType = mediaType;

        LOG.debug("Created Link Object {}", this);
    }

    public Link1(String name, AppealsUri uri) {
        this(name, uri, Representation1.APPEALS_MEDIA_TYPE);
    }

    public String getRelValue() {
        LOG.debug("Retrieving the rel attribute value {}", rel);
        return rel;
    }

    public URI getUri() {
        
        try {
            URI local_uri = new URI(uri);
            LOG.debug("Retrieving the uri attribute value {}", local_uri);
            return local_uri;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMediaType() {
        LOG.debug("Retrieving the media type attribute value {}", mediaType);
        return mediaType;
    }
}
