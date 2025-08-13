package api.preRegistro.service;

import api.preRegistro.entity.CityEntity;
import api.preRegistro.entity.CountryEntity;
import api.preRegistro.entity.MunicipalityEntity;
import api.preRegistro.entity.StateEntity;
import api.preRegistro.repository.CityRepository;
import api.preRegistro.repository.CountryRepository;
import api.preRegistro.repository.MunicipalityRepository;
import api.preRegistro.repository.StateRepository;
import api.preRegistro.util.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectionService {

    private final CountryRepository countryRepository;

    private final StateRepository stateRepository;

    private final CityRepository cityRepository;

    private final MunicipalityRepository municipalityRepository;

    public DirectionService(CountryRepository countryRepository, StateRepository stateRepository, CityRepository cityRepository, MunicipalityRepository municipalityRepository) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.municipalityRepository = municipalityRepository;
    }

    public Response getAllCountry() {
        List<CountryEntity> countryEntities;
        countryEntities = countryRepository.findAll();

        if (countryEntities.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", countryEntities);
        }
    }

    public Response getAllCountryByMunicipality(Long idMunicipality) {
        List<CityEntity> cityEntities;
        cityEntities = cityRepository.getAllCitiesByMunicipality(idMunicipality);

        if (cityEntities.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", cityEntities);
        }
    }

    public Response getAllState() {
        List<StateEntity> stateEntities;
        stateEntities = stateRepository.findAll();

        if (stateEntities.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", stateEntities);
        }
    }

    public Response getAllStateByIdCountry(Long idCountry) {
        List<StateEntity> stateEntities;
        stateEntities = stateRepository.getAllStateByCountry(idCountry);

        if (stateEntities.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", stateEntities);
        }
    }

    public Response getAllCity() {
        List<CityEntity> cityEntities;
        cityEntities = cityRepository.findAll();

        if (cityEntities.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", cityEntities);
        }
    }

    public Response getAllMunicipality() {
        List<MunicipalityEntity> municipalityEntities;
        municipalityEntities = municipalityRepository.findAll();

        if (municipalityEntities.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", municipalityEntities);
        }
    }

    public Response getAllMunicipalityByStateId(Long idState) {
        List<MunicipalityEntity> municipalityEntities;
        municipalityEntities = municipalityRepository.getAllCitiesByMunicipality(idState);

        if (municipalityEntities.isEmpty()) {
            return new Response("ERROR", "No data");
        } else {
            return new Response("SUCCESS", municipalityEntities);
        }
    }
}
