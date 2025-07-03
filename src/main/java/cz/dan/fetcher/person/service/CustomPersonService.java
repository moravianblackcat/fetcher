package cz.dan.fetcher.person.service;

import cz.dan.avro.fetcher.request.PersonRequest;

public interface CustomPersonService {

    void save(PersonRequest request);

}
