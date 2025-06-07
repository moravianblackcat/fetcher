package cz.dan.fetcher.domain.outbox.exception.resource;

import lombok.experimental.StandardException;

@StandardException
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceIdentifier, String resourceType, String source) {
        super("Resource ID %s for type %s not found in %s.".formatted(resourceIdentifier, resourceType, source));
    }

}
