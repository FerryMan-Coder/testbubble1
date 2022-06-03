package nl.hva.miw.c27.team1.cryptobanking.repository.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.hva.miw.c27.team1.cryptobanking.model.Asset;
import nl.hva.miw.c27.team1.cryptobanking.model.Customer;
import nl.hva.miw.c27.team1.cryptobanking.model.Portfolio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcPortfolioDao implements PortfolioDao {

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(JdbcPortfolioDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcPortfolioDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcPortfolioDao.");
    }

    @Override
    public double findQuantityOfAssetInPortfolio (String assetCode, int userId) {
        String sql = "SELECT * FROM assetofcustomer WHERE assetCode = ? AND userId = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, assetCode, userId);
        } catch (EmptyResultDataAccessException e) {
            e.getMessage();
            return 0.0;
        }
    }

    private static class AssetRowMapper implements RowMapper<Asset> {
        @Override
        public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Asset(rs.getString("assetCode"),
                    rs.getString("assetName"), rs.getDouble("rateInEuro"));
        }
    }

    private PreparedStatement insertPortfolioStatement(Portfolio portfolio, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "insert into assetofcustomer (assetCode, userId, quantityOfAsset) values (?, ?, ?)");
        for(Map.Entry<Asset, Double>  map : portfolio.getAssetsOfUser().entrySet()) {
            ps.setString(1, map.getKey().getAssetCode());
            ps.setInt(2, portfolio.getCustomer().getId());
            ps.setDouble(3, map.getValue());
        }
        return ps;
    }

    @Override
    public void save(Portfolio portfolio) {
        jdbcTemplate.update(connection -> insertPortfolioStatement(portfolio, connection));
    }

    @Override
    public Portfolio getPortfolio(Customer customer) {
        int userId = customer.getId();
        // lijst waar 1 portfolio in zit?
        List<Portfolio> assetList = jdbcTemplate.query
                ("SELECT * FROM assetofcustomer  WHERE userId = ?",
                        new JdbcPortfolioDao.PortfolioRowMapper(), userId);
        if (assetList.size() < 1) {
            System.out.println("Geen assets");
            return null;
        } else {
            // check if correct
            int listSize = assetList.size() -1;
            return assetList.get(-listSize);
        }
    }

    public Optional<Portfolio> findById(int id) {
        List<Portfolio> portfolioList =
                jdbcTemplate.query("SELECT * FROM assetofcustomer WHERE userId = ?",
                        new JdbcPortfolioDao.PortfolioRowMapper(), id);
        if (portfolioList.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(portfolioList.get(0));
        }
    }

    private static class PortfolioRowMapper implements RowMapper<Portfolio> {
        @Override
        public Portfolio mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Map<Asset, Double> assetsOfUser = new HashMap<>();
            String assetCode = resultSet.getString("assetCode");
            int userId = resultSet.getInt("userId");
            double quantityOfAsset = resultSet.getDouble("quantityOfAsset");
            assetsOfUser.put(new Asset(assetCode),quantityOfAsset);
            JdbcUserDao jdbcUserDao = new JdbcUserDao(new JdbcTemplate());
            // check of casting is done correctly
            Portfolio portfolio =
                    new Portfolio(assetsOfUser, (Customer) jdbcUserDao.findById(userId).orElse(null));
            return portfolio;
        }
    }


}





