/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.common.account.persist;

import chuks.flatbok.fx.common.account.order.ManagedOrder;
import chuks.flatbok.fx.common.account.order.OrderException;
import chuks.flatbok.fx.common.account.order.SymbolInfo;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class OrderDB {

    private static final String DB_URL = "jdbc:sqlite:fix_client_data_db.db";

    private static final String CREATE_TABLE_SQL_FOR_ORDER_ID
            = "CREATE TABLE IF NOT EXISTS order_ids ("
            + "id INTEGER PRIMARY KEY,"
            + "last_id INTEGER NOT NULL);";

    private static final String INSERT_INITIAL_VALUE_SQL_FOR_ORDER_ID
            = "INSERT INTO order_ids (id, last_id) "
            + "SELECT 1, 1000000000 WHERE NOT EXISTS (SELECT * FROM order_ids);";

    private static final String CREATE_TABLE_SQL_FOR_SELECTED_SYMBOLS
            = "CREATE TABLE IF NOT EXISTS selected_symbols ("
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " name TEXT NOT NULL UNIQUE,"
            + " digits REAL,"
            + " spread REAL,"
            + " tick_value REAL,"
            + " tick_size REAL,"
            + " bid REAL,"
            + " ask REAL,"
            + " price REAL,"
            + " swap REAL"
            + ");";

    private static final String CREATE_TABLE_SQL_FOR_OPEN_ORDERS
            = "CREATE TABLE IF NOT EXISTS open_orders ("
            + " order_id TEXT PRIMARY KEY,"
            + " account_number INTEGER NOT NULL,"
            + " request_identifier TEST NULL,"                        
            + " symbol TEXT NOT NULL,"
            + " open_price REAL NOT NULL,"
            + " digits REAL NOT NULL,"
            + " lot_size REAL NOT NULL,"
            + " side CHAR(1) NOT NULL,"
            + " target_price REAL,"
            + " stoploss_price REAL,"
            + " commission REAL,"
            + " swap REAL,"
            + " open_time DATETIME NOT NULL"
            + ");";

    private static final String CREATE_TABLE_SQL_FOR_HISTORY_ORDERS
            = "CREATE TABLE IF NOT EXISTS history_orders ("
            + " order_id TEXT PRIMARY KEY,"
            + " symbol TEXT NOT NULL,"
            + " open_price REAL NOT NULL,"
            + " close_price REAL NOT NULL,"
            + " digits REAL NOT NULL,"
            + " lot_size REAL NOT NULL,"
            + " side CHAR(1) NOT NULL,"
            + " target_price REAL,"
            + " stoploss_price REAL,"
            + " commission REAL,"
            + " swap REAL,"
            + " open_time DATETIME NOT NULL,"
            + " close_time DATETIME NOT NULL"
            + ");";

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setDriverClassName("org.sqlite.JDBC"); // Ensure you have the SQLite JDBC driver
        config.setMaximumPoolSize(50); // Set pool size
        config.setConnectionTimeout(30000);  // 30 seconds timeout
        config.setIdleTimeout(600000);  // 10 minutes idle timeout
        config.setMaxLifetime(1800000);  // 30 minutes max lifetime
        
        dataSource = new HikariDataSource(config);

        try {
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL_FOR_ORDER_ID);
            stmt.execute(INSERT_INITIAL_VALUE_SQL_FOR_ORDER_ID);
            stmt.execute(CREATE_TABLE_SQL_FOR_SELECTED_SYMBOLS);
            stmt.execute(CREATE_TABLE_SQL_FOR_OPEN_ORDERS);
            stmt.execute(CREATE_TABLE_SQL_FOR_HISTORY_ORDERS);
        }
    }

    public static synchronized String getNewID() throws SQLException {
        long newOrderID = 0;

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery("SELECT last_id FROM order_ids WHERE id = 1");
            if (rs.next()) {
                long lastID = rs.getLong("last_id");
                newOrderID = lastID + 1;

                stmt.executeUpdate("UPDATE order_ids SET last_id = " + newOrderID + " WHERE id = 1");
            }

            conn.commit();
        }

        return "" + newOrderID;
    }

    //NOT COMPLETED
    public static synchronized void replaceSymbolInfoList(LinkedList<SymbolInfo> symbols) {
        String deleteSql = "DELETE FROM selected_symbols";
        String insertSql = "INSERT INTO selected_symbols(name, digits, spread, tick_value, tick_size, bid, ask, price, swap) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            // Delete all rows
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteSql);
            }

            // Insert new symbols
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                for (SymbolInfo symbol : symbols) {
                    pstmt.setString(1, symbol.getName());
                    pstmt.setDouble(2, symbol.getPipettePoint());
                    pstmt.setDouble(3, symbol.getSpreadPip());
                    pstmt.setDouble(4, symbol.getTickValue());
                    pstmt.setDouble(5, symbol.getTickSize());
                    pstmt.setDouble(6, symbol.getBid());
                    pstmt.setDouble(7, symbol.getAsk());
                    pstmt.setDouble(8, symbol.getPrice());
                    pstmt.setDouble(9, symbol.getSwapLong());
                    pstmt.setDouble(10, symbol.getSwapShort());
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //NOT COMPLETED
    public static synchronized LinkedList<SymbolInfo> getSelectedSymbolInfoList() {
        String sql = "SELECT name, digits, spread, tick_value, tick_size, bid, ask, price, swap FROM selected_symbols";
        LinkedList<SymbolInfo> selectedSymbols = new LinkedList<>();

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                int digits = rs.getInt("digits");
                double tickValue = rs.getDouble("tick_value");
                double tickSize = rs.getDouble("tick_size");
                double bid = rs.getDouble("bid");
                double ask = rs.getDouble("ask");

                SymbolInfo symbolInfo = new SymbolInfo(name, digits, tickValue, tickSize);
                symbolInfo.setBid(bid);
                symbolInfo.setAsk(ask);

                selectedSymbols.add(symbolInfo);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return selectedSymbols;
    }

    //NOT COMPLETED
    public static synchronized void removeSelectedSymbolInfo(String selectedSymbol) {
        String sql = "DELETE FROM selected_symbols WHERE name = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, selectedSymbol);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized void insertHistoryOrder(ManagedOrder order) {
        String insertSQL = "INSERT INTO history_orders(order_id, account_number, open_price, close_price, digits, lot_size, side, symbol, target_price, stoploss_price, commission, swap, open_time, close_time) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, order.getOrderID());
            pstmt.setInt(2, order.getAccountNumber());
            pstmt.setDouble(3, order.getOpenPrice());
            pstmt.setDouble(4, order.getClosePrice());
            pstmt.setInt(5, order.getSymbolDigits());
            pstmt.setDouble(6, order.getLotSize());
            pstmt.setString(7, String.valueOf(order.getSide()));
            pstmt.setString(8, order.getSymbol());
            pstmt.setDouble(9, order.getTargetPrice());
            pstmt.setDouble(10, order.getStoplossPrice());
            pstmt.setDouble(11, order.getCommission());
            pstmt.setDouble(12, order.getSwap());
            pstmt.setLong(13, order.getOpenTime() != null ? order.getOpenTime().getTime() : 0);
            pstmt.setLong(14, order.getCloseTime() != null ? order.getCloseTime().getTime() : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized void insertOpenOrder(ManagedOrder order) {
        String insertSQL = "INSERT INTO open_orders(order_id, account_number, open_price, close_price, digits, lot_size, side, symbol, target_price, stoploss_price, commission, swap, open_time, close_time) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, order.getOrderID());
            pstmt.setInt(2, order.getAccountNumber());
            pstmt.setDouble(3, order.getOpenPrice());
            pstmt.setDouble(4, order.getClosePrice());
            pstmt.setInt(5, order.getSymbolDigits());
            pstmt.setDouble(6, order.getLotSize());
            pstmt.setString(7, String.valueOf(order.getSide()));
            pstmt.setString(8, order.getSymbol());
            pstmt.setDouble(9, order.getTargetPrice());
            pstmt.setDouble(10, order.getStoplossPrice());
            pstmt.setDouble(11, order.getCommission());
            pstmt.setDouble(12, order.getSwap());
            pstmt.setLong(13, order.getOpenTime() != null ? order.getOpenTime().getTime() : 0);
            pstmt.setLong(14, order.getCloseTime() != null ? order.getCloseTime().getTime() : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized void replaceOpenOrderList(LinkedList<ManagedOrder> orders) {
        String deleteSql = "DELETE FROM open_orders";
        String insertSql = "INSERT INTO open_orders(order_id, account_number, symbol, open_price, digits, lot_size, side, target_price, stoploss_price, commission, swap, open_time) VALUES(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            // Delete all rows
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteSql);
            }

            // Insert new orders
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                for (ManagedOrder order : orders) {
                    pstmt.setString(1, order.getOrderID());
                    pstmt.setInt(2, order.getAccountNumber());
                    pstmt.setString(3, order.getSymbol());
                    pstmt.setDouble(4, order.getOpenPrice());
                    pstmt.setInt(5, order.getSymbolDigits());
                    pstmt.setDouble(6, order.getLotSize());
                    pstmt.setString(7, String.valueOf(order.getSide()));
                    pstmt.setDouble(8, order.getTargetPrice());
                    pstmt.setDouble(9, order.getStoplossPrice());
                    pstmt.setDouble(10, order.getCommission());
                    pstmt.setDouble(11, order.getSwap());
                    pstmt.setTimestamp(12, new Timestamp(order.getOpenTime().getTime()));
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized LinkedList<ManagedOrder> getOpenOrderList() {
        String sql = "SELECT order_id,account_number, request_identifier,symbol, open_price, digits, lot_size, side, target_price, stoploss_price, commission, swap, open_time FROM open_orders";
        LinkedList<ManagedOrder> orders = new LinkedList<>();

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String orderID = rs.getString("order_id");
                int accountNumber = rs.getInt("account_number");
                String request_identifier = rs.getString("request_identifier");
                String symbol = rs.getString("symbol");
                double openPrice = rs.getDouble("open_price");
                int digits = rs.getInt("digits");
                double lotSize = rs.getDouble("lot_size");
                char side = rs.getString("side").charAt(0);
                double targetPrice = rs.getDouble("target_price");
                double stoplossPrice = rs.getDouble("stoploss_price");
                double commission = rs.getDouble("commission");
                double swap = rs.getDouble("swap");
                Date openTime = rs.getTimestamp("open_time");

                SymbolInfo symbolInfo = new SymbolInfo(symbol, digits, 0, 0); // Adjust this to match your SymbolInfo constructor
                ManagedOrder order = new ManagedOrder(request_identifier, accountNumber, symbolInfo, side, targetPrice, stoplossPrice);
                order.setOrderID(orderID);
                order.setOpenPrice(openPrice);
                order.setLotSize(lotSize);
                order.setCommission(commission);
                order.setSwap(swap);
                order.setOpenTime(openTime);

                orders.add(order);
            }
        } catch (SQLException | OrderException e) {
            System.out.println(e.getMessage());
        }

        return orders;
    }

    public static synchronized void replaceHistoryOrderList(LinkedList<ManagedOrder> orders) {
        String deleteSql = "DELETE FROM history_orders";
        String insertSql = "INSERT INTO history_orders(order_id, account_number, symbol, open_price, close_price, digits, lot_size, side, target_price, stoploss_price, commission, swap, open_time, close_time) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            // Delete all rows
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteSql);
            }

            // Insert new orders
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                for (ManagedOrder order : orders) {
                    pstmt.setString(1, order.getOrderID());
                    pstmt.setInt(2, order.getAccountNumber());
                    pstmt.setString(3, order.getSymbol());
                    pstmt.setDouble(4, order.getOpenPrice());
                    pstmt.setDouble(5, order.getClosePrice());
                    pstmt.setInt(6, order.getSymbolDigits());
                    pstmt.setDouble(7, order.getLotSize());
                    pstmt.setString(8, String.valueOf(order.getSide()));
                    pstmt.setDouble(9, order.getTargetPrice());
                    pstmt.setDouble(10, order.getStoplossPrice());
                    pstmt.setDouble(11, order.getCommission());
                    pstmt.setDouble(12, order.getSwap());
                    pstmt.setTimestamp(13, new Timestamp(order.getOpenTime().getTime()));
                    pstmt.setTimestamp(14, new Timestamp(order.getCloseTime().getTime()));
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized LinkedList<ManagedOrder> getHistoryOrderList() {
        String sql = "SELECT order_id, account_number, request_identifier, symbol, open_price, close_price, digits, lot_size, side, target_price, stoploss_price, commission, swap, open_time, close_time FROM history_orders";
        LinkedList<ManagedOrder> orders = new LinkedList<>();

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int accountNumber = rs.getInt("account_number");
                String request_identifier = rs.getString("request_identifier");
                String orderID = rs.getString("order_id");
                String symbol = rs.getString("symbol");
                double openPrice = rs.getDouble("open_price");
                double closePrice = rs.getDouble("close_price");
                int digits = rs.getInt("digits");
                double lotSize = rs.getDouble("lot_size");
                char side = rs.getString("side").charAt(0);
                double targetPrice = rs.getDouble("target_price");
                double stoplossPrice = rs.getDouble("stoploss_price");
                double commission = rs.getDouble("commission");
                double swap = rs.getDouble("swap");
                Date openTime = rs.getTimestamp("open_time");
                Date closeTime = rs.getTimestamp("close_time");

                SymbolInfo symbolInfo = new SymbolInfo(symbol, digits, 0, 0); // Adjust this to match your SymbolInfo constructor
                ManagedOrder order = new ManagedOrder(request_identifier, accountNumber, symbolInfo, side, targetPrice, stoplossPrice);
                order.setOrderID(orderID);
                order.setOpenPrice(openPrice);
                order.setClosePrice(closePrice);
                order.setLotSize(lotSize);
                order.setCommission(commission);
                order.setSwap(swap);
                order.setOpenTime(openTime);
                order.setCloseTime(closeTime);

                orders.add(order);
            }
        } catch (SQLException | OrderException e) {
            System.out.println(e.getMessage());
        }

        return orders;
    }

    public static synchronized void removeOpenOrder(String orderID) {
        String sql = "DELETE FROM open_orders WHERE order_id = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized void removeHistoryOrder(String orderID) {
        String sql = "DELETE FROM history_orders WHERE order_id = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
