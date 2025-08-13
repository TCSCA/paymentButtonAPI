package api.authentication.service;

import api.authentication.projection.TransaccionProjection;
import api.authentication.repository.TransaccionRepository;
import api.authentication.to.TransaccionTo;
import api.authentication.util.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    public Response getTransaccionByProfileAndSeccion(final Long idUser, final Long idSeccion){

        List<TransaccionProjection> transaccionEntities;

        transaccionEntities = transaccionRepository.findByIdProfileAndIdSeccion(idUser, idSeccion);

        List<TransaccionTo> transaccionToList = transaccionEntities.stream()
                .map(projection -> new TransaccionTo(projection.getIdTransaccion(),
                        projection.getTransaccion())).toList();

        if (transaccionToList.size() == 0){
            return new Response("ERROR", transaccionToList);
        }

        return new Response("SUCCESS", transaccionToList);
    }

}
