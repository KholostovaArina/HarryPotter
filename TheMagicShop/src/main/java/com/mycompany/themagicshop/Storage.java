package com.mycompany.themagicshop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    public static List<MagicWand> getAllWands() {
        List<MagicWand> wands = new ArrayList<>();
        String sql = "SELECT * FROM magic_shop.magic_wand";

        try (Connection conn = PostgresConnecter.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Обработка NULL для name_owner
                String ownerName = rs.getString("name_owner");
                if (rs.wasNull()) {
                    ownerName = null;
                }

                // Обработка NULL для date_of_purchase
                LocalDate purchaseDate = null;
                Date date = rs.getDate("date_of_purchase");
                if (date != null) {
                    purchaseDate = date.toLocalDate();
                }

                MagicWand wand = new MagicWand(
                        rs.getInt("id"),
                        rs.getString("корпус"),
                        rs.getString("сердцевина"),
                        rs.getString("статус"),
                        ownerName,
                        purchaseDate
                );
                wands.add(wand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wands;
    }

    public static List<Supply> getAllSupplies() {
        List<Supply> supplies = new ArrayList<>();
        String sql = "SELECT * FROM magic_shop.supply";

        try (Connection conn = PostgresConnecter.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate supplyDate = rs.getDate("data").toLocalDate();
                boolean inWarehouse = (rs.getString("inWarehouse")).equals("да");
                Supply supply = new Supply(
                        rs.getInt("id"),
                        supplyDate,
                        inWarehouse
                );
                supplies.add(supply);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplies;
    }

    public static List<Warehouse> getWarehouse() {
        List<Warehouse> warehouseItems = new ArrayList<>();
        String sql = "SELECT * FROM magic_shop.warehouse";

        try (Connection conn = PostgresConnecter.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Warehouse item = new Warehouse(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("id_supply")
                );
                warehouseItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouseItems;
    }

    public static void addMagicWand(MagicWand wand) {
        String sql = "INSERT INTO magic_shop.magic_wand (корпус, сердцевина, статус, name_owner, date_of_purchase) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = PostgresConnecter.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wand.getCorpus());
            pstmt.setString(2, wand.getCore());
            pstmt.setString(3, wand.getStatus());

            if (wand.getOwnerName() != null) {
                pstmt.setString(4, wand.getOwnerName());
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }

            if (wand.getPurchaseDate() != null) {
                pstmt.setDate(5, Date.valueOf(wand.getPurchaseDate()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWand(MagicWand wand) {
        String sql = "UPDATE magic_shop.magic_wand SET "
                + "статус = ?, name_owner = ?, date_of_purchase = ? "
                + "WHERE id = ?";

        try (Connection conn = PostgresConnecter.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wand.getStatus());
            pstmt.setString(2, wand.getOwnerName());
            pstmt.setDate(3, Date.valueOf(wand.getPurchaseDate()));
            pstmt.setInt(4, wand.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Warehouse getWarehouseItem(String type, String name) {
        String sql = "SELECT * FROM magic_shop.warehouse WHERE type = ? AND name = ?";

        try (Connection conn = PostgresConnecter.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setString(2, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Warehouse(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getInt("id_supply")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateWarehouseQuantity(int id, int newQuantity) {
        String sql;
        if (newQuantity <= 0) {
            sql = "DELETE FROM magic_shop.warehouse WHERE id = ?";
        } else {
            sql = "UPDATE magic_shop.warehouse SET quantity = ? WHERE id = ?";
        }

        try (Connection conn = PostgresConnecter.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (newQuantity <= 0) {
                pstmt.setInt(1, id);
            } else {
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, id);
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateWarehouse(int id, int newQuantity) {
        String sql;
        boolean isDelete = (newQuantity <= 0);
        
        if (isDelete) {
            sql = "DELETE FROM magic_shop.warehouse WHERE id = ?";
        } else {
            sql = "UPDATE magic_shop.warehouse SET quantity = ? WHERE id = ?";
        }

        try (Connection conn = PostgresConnecter.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (isDelete) {
                pstmt.setInt(1, id);
            } else {
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, id);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
