import java.sql.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url = "jdbc:postgresql://localhost:5432/Inventory";
        ResultSet res = null;
        Connection con = null;
        Statement stat = null;

        String sqlCreate = "CREATE TABLE technics (Id INT PRIMARY KEY, ProductName VARCHAR(30), Type VARCHAR(30), Count INT, Price INT)";
        String sqlAdd = "INSERT INTO technics(Id, ProductName, Type, Count, Price) VALUES " +
                "(1,'Iphone 15', 'Phone', 5, 600000)," +
                "(2,'Logitech G102', 'Mouse', 10, 42000), " +
                "(3,'PlayStation 5', 'Console', 3, 360000), " +
                "(4,'Airpods 3','Headphones',7,120000)";

        String sqlUpdate = "UPDATE technics SET Price = 300000 WHERE ProductName = 'PlayStation 5'";
        String sqlDeleteOne = "DELETE FROM technics WHERE ProductName = 'Logitech G102'";
        String sqlDelete = "DROP TABLE technics";
        String sqlWatch = "SELECT * FROM technics";
        try(Connection conn = DriverManager.getConnection(url, "postgres", "LastHope7")) {
            DatabaseMetaData metaData = conn.getMetaData();
            String tableName = "technics"; // Имя таблицы для проверки существования
            Class.forName("org.postgresql.Driver");
            // Получаем метаданные о таблицах в базе данных
            ResultSet tables = metaData.getTables(null, null, tableName, null);
            if (tables.next()) {
                Statement statement = conn.createStatement();
                System.out.println("Таблица с именем " + tableName + " существует.");
                System.out.println("Что вы хотите сделать с таблицой?: ");
                System.out.println("1.Удалить одну строку\n2.Обновить\n3.Посмотреть\n4.Удалить таблицу");
                int n = scanner.nextInt();
                switch (n){
                    case 1: statement.executeUpdate(sqlDeleteOne);
                        System.out.println("Часть таблицы удалена");
                        ResultSet resultSet = statement.executeQuery(sqlWatch);
                        while (resultSet.next()) {
                            System.out.println(resultSet.getInt("Id") + " " + resultSet.getString("ProductName") + " " +
                                     resultSet.getString("Type") + " " + resultSet.getInt("Count") + " " + resultSet.getInt("Price"));
                        }
                        break;
                    case 2: statement.executeUpdate(sqlUpdate);
                        System.out.println("Таблица обновлена");
                        ResultSet resultSet1 = statement.executeQuery(sqlWatch);
                        while (resultSet1.next()) {
                            System.out.println(resultSet1.getInt("Id") + " " + resultSet1.getString("ProductName") + " " +
                                    resultSet1.getString("Type") + " " + resultSet1.getInt("Count") + " " + resultSet1.getInt("Price"));
                        }
                        break;
                    case 3:
                        ResultSet resultSet2 = statement.executeQuery(sqlWatch);
                        while (resultSet2.next()) {
                            System.out.println(resultSet2.getInt("Id") + " " + resultSet2.getString("ProductName") + " " +
                                    resultSet2.getString("Type") + " " + resultSet2.getInt("Count") + " " + resultSet2.getInt("Price"));
                        }
                        break;
                    case 4: statement.executeUpdate(sqlDelete);
                        System.out.println("Таблица удалена");
                        break;
                    default:
                        System.out.println("Введите цифру от 1 до 5");
                        break;
                }
            } else {
                Statement statement = conn.createStatement();
                statement.executeUpdate(sqlCreate);
                System.out.println("Таблица создана");
                statement.executeUpdate(sqlAdd);
                ResultSet resultSet3 = statement.executeQuery(sqlWatch);
                while (resultSet3.next()) {
                    System.out.println(resultSet3.getInt("Id") + " " + resultSet3.getString("ProductName") + " " +
                            resultSet3.getString("Type") + " " + resultSet3.getInt("Count") + " " +  resultSet3.getInt("Price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
