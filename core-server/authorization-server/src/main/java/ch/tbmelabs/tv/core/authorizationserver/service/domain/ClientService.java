package ch.tbmelabs.tv.core.authorizationserver.service.domain;

import ch.tbmelabs.tv.core.authorizationserver.domain.Client;
import ch.tbmelabs.tv.core.authorizationserver.domain.dto.ClientDTO;
import ch.tbmelabs.tv.core.authorizationserver.domain.dto.mapper.ClientMapper;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.ClientAuthorityAssociationCRUDRepository;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.ClientCRUDRepository;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.ClientGrantTypeAssociationCRUDRepository;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.ClientScopeAssociationCRUDRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

  @Autowired
  private ClientMapper clientMapper;

  @Autowired
  private ClientCRUDRepository clientRepository;

  @Autowired
  private ClientGrantTypeAssociationCRUDRepository clientGrantTypeRepository;

  @Autowired
  private ClientAuthorityAssociationCRUDRepository clientAuthorityRepository;

  @Autowired
  private ClientScopeAssociationCRUDRepository clientScopeRepository;

  @Transactional
  public Client save(ClientDTO clientDTO) {
    if (clientDTO.getId() != null) {
      throw new IllegalArgumentException("You can only create a new client without an id!");
    }

    Client client = clientMapper.toEntity(clientDTO);
    client = clientRepository.save(client);

    clientMapper.grantTypesToGrantTypeAssociations(clientDTO.getGrantTypes(), client)
        .forEach(clientGrantTypeRepository::save);
    clientMapper.authoritiesToAuthorityAssociations(clientDTO.getGrantedAuthorities(), client)
        .forEach(clientAuthorityRepository::save);
    clientMapper.scopesToScopeAssociations(clientDTO.getScopes(), client)
        .forEach(clientScopeRepository::save);

    return client;
  }

  public Page<ClientDTO> findAll(Pageable pageable) {
    return clientRepository.findAll(pageable).map(clientMapper::toDto);
  }

  public Client update(ClientDTO clientDTO) {
    if (clientDTO.getId() == null || clientRepository.findOne(clientDTO.getId()) == null) {
      throw new IllegalArgumentException("You can only update an existing client!");
    }

    Client client = clientMapper.toEntity(clientDTO);
    client = clientRepository.save(client);

    clientMapper.grantTypesToGrantTypeAssociations(clientDTO.getGrantTypes(), client)
        .forEach(clientGrantTypeRepository::save);
    clientMapper.authoritiesToAuthorityAssociations(clientDTO.getGrantedAuthorities(), client)
        .forEach(clientAuthorityRepository::save);
    clientMapper.scopesToScopeAssociations(clientDTO.getScopes(), client)
        .forEach(clientScopeRepository::save);

    return client;
  }

  public void delete(ClientDTO clientDTO) {
    if (clientDTO.getId() == null) {
      throw new IllegalArgumentException("You can only delete an existing client!");
    }

    clientRepository.delete(clientMapper.toEntity(clientDTO));
  }
}
