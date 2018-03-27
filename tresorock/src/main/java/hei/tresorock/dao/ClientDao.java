package hei.tresorock.dao;

import hei.tresorock.entities.Client;

import java.util.List;

public interface ClientDao {

    public List<Client> listClient();

    public Client getClient(Integer idClient);

    public Client addClient(Client client);

}