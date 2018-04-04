package hei.tresorock.dao.DataBaseCommunication;

import hei.tresorock.dao.SoireeDao;
import hei.tresorock.entities.Soiree;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet de faire le lien avec la table Soiree dans la BDD
 */
public class SoireeDaoImpl implements SoireeDao {
    /**
     * Cette méthode retourne une liste de toutes les soirées dans la BDD dans une structure List
     *
     * @return une List<> d'objets Soiree
     */
    @Override
    public List<Soiree> listSoiree() {
        String query = "SELECT * FROM Soiree ORDER BY dateSoiree";
        List<Soiree> listOfSoiree = new ArrayList<>();
        try (
                Connection connection = DataBaseProvider.getdataBase().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                listOfSoiree.add(
                        new Soiree(
                                resultSet.getInt("idSoiree"),
                                resultSet.getDate("DateSoiree").toLocalDate(),
                                resultSet.getDouble("RecetteDeCaisse"),
                                resultSet.getDouble("ErreurDeCaisse"),
                                resultSet.getString("Theme")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfSoiree;
    }

    /**
     * Cette méthode retourne un objet Soiree correpsondant à l'identifiant unique de la soirée, passé en paramètre
     *
     * @param idSoiree de la soirée recherchée
     * @return null
     */
    @Override
    public Soiree getSoiree(Integer idSoiree) {
        String query = "SELECT * FROM Soiree WHERE idSoiree=?";
        try (
                Connection connection = DataBaseProvider.getdataBase().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idSoiree);
            try (
                    ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Soiree(
                            resultSet.getInt("idSoiree"),
                            resultSet.getDate("DateSoiree").toLocalDate(),
                            resultSet.getDouble("RecetteDeCaisse"),
                            resultSet.getDouble("ErreurDeCaisse"),
                            resultSet.getString("Theme"));

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Cette méthode permet d'ajouter une soiree dans la base de données
     * @param soiree - soiree à ajouter
     * @return null
     */

    @Override
    public Soiree addSoiree(Soiree soiree) {
        String query = "INSERT INTO Soiree(DateSoiree, RecetteDeCaisse, ErreurDeCaisse, Theme) VALUES (?, ?, ?, ?)";
        try (
                Connection connection = DataBaseProvider.getdataBase().getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setDate(1,Date.valueOf(soiree.getDateSoiree()));
            statement.setDouble(2, soiree.getRecetteCaisse());
            statement.setDouble(3, soiree.getErreurCaisse());
            statement.setString(4,soiree.getThemeSoiree());
            statement.executeUpdate();

            try (ResultSet ids = statement.getGeneratedKeys()) {
                if (ids.next()) {
                    int generatedId = ids.getInt(1);
                    soiree.setIdSoiree(generatedId);
                    return soiree;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}