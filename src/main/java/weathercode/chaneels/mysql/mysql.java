package weathercode.chaneels.mysql;

import org.bukkit.GameMode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import weathercode.chaneels.Utils.Util;
import weathercode.chaneels.main.Main;

import java.sql.*;
import java.util.UUID;

public class mysql {
    private static Main plugins;
    private static Connection conn;

    public mysql(Main plugins) {
        mysql.plugins = plugins;
    }

    public static void setConn() {
        String host = plugins.getConfig().getString("bazadanych.host");
        String user = plugins.getConfig().getString("bazadanych.user");
        String password = plugins.getConfig().getString("bazadanych.password");
        String base = plugins.getConfig().getString("bazadanych.base");
        String port = plugins.getConfig().getString("bazadanych.port");

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + base, user, password);
            plugins.getLogger().info(Util.helpcolor("&a[W-CHANEELS] Polaczono sie z baza danych"));
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setConn() : " + e));
        }

    }

    public static void createTable() {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS chaneelsusers(uuid VARCHAR(255) NOT NULL, X VARCHAR(255) NOT NULL, Y VARCHAR(255) NOT NULL, Z VARCHAR(255) NOT NULL, INV VARCHAR(2048) NOT NULL, lastsector VARCHAR(255) NOT NULL, effects VARCHAR(2048) NOT NULL, gm VARCHAR(255) NOT NULL, health VARCHAR(255), slot VARCHAR(255), arrmor VARCHAR(1024) NOT NULL, feed VARCHAR(255) NOT NULL, exp VARCHAR(255) NOT NULL, playerdirection VARCHAR(510) NOT NULL, enderchest VARCHAR(2048) NOT NULL, PRIMARY KEY (uuid));");
            plugins.getLogger().info("&a&l[W-CHANEELS] Wykonano kod createTable()");
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z createTable() " + e));
        }
    }
    public static void createTable2() {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS chaneels(name VARCHAR(255) NOT NULL, tps VARCHAR(255) NOT NULL, players VARCHAR(255) NOT NULL, status VARCHAR(255), PRIMARY KEY (name));");
            plugins.getLogger().info("&a&l[W-CHANEELS] Wykonano kod createTable2()");
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z createTable2() " + e));
        }
    }
    public static void addchaneel(String name, String tps, double players, String status) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO chaneels(name, tps, players, status) VALUES ('" + name +"', '" + tps + "', '" + players + "','" + status + "');");
            plugins.getLogger().info("&a&l[W-CHANEELS] Wykonano kod adduser()");
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z adduser() " + e));
        }
    }
    public static boolean ischaneeladded(String name) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM chaneels WHERE name = ?");
            ps.setString(1, name);
            plugins.getLogger().info("&a&l[W-CHANEELS] Wykonano kod ischaneeladded()");
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z ischaneeladded() :" + e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugins.getLogger().info(Util.helpcolor("&cBlad podczas zamykania zasobów: " + e));
            }
        }

        return false;
    }
    public static String loadchaneels() {
        JSONArray sectorData = new JSONArray();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM chaneels");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sectorName = rs.getString("name");
                String status = rs.getString("status");
                String tps = rs.getString("tps");
                String players = rs.getString("players");

                JSONObject sectorObject = new JSONObject();
                sectorObject.put("name", sectorName);
                sectorObject.put("status", status);
                sectorObject.put("tps", tps);
                sectorObject.put("players", players);

                sectorData.add(sectorObject);
            }

            rs.close();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z loadSectors() " + e));
        }
        return sectorData.toJSONString();
    }
    public static void settps(String name, double tps) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneels SET tps = '" + tps + "' WHERE name = '" + name + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod settps()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z settps() : " + e));
        }
    }
    public static void setplayers(String name, int players) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneels SET players = '" + players + "' WHERE name = '" + name + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setplayers()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setplayers() : " + e));
        }
    }
    public static void setstatus(String name, String status) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneels SET status = '" + status + "' WHERE name = '" + name + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setstatus()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setstatus() : " + e));
        }
    }


    public static void adduser(UUID u, Double x, Double y, Double z, String inv, String arrmor, String lastsector, String enderchest) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO chaneelsusers(uuid, X, Y, Z, INV, lastsector, gm, effects, health, slot, arrmor, feed, exp, playerdirection, enderchest) VALUES ('" + u + "','" + x + "','" + y + "','" + z + "','" + inv + "','" + lastsector + "', '0', '', '20', '0','" + arrmor + "', '20', '0', '','" + enderchest + "');");
            plugins.getLogger().info("&a&l[W-CHANEELS] Wykonano kod adduser()");
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z adduser() " + e));
        }
    }

    public static boolean isuseradded(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info("&a&l[W-CHANEELS] Wykonano kod isuseradded()");
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z isuseradded() :" + e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugins.getLogger().info(Util.helpcolor("&cBlad podczas zamykania zasobów: " + e));
            }
        }

        return false;
    }

    public static double getX(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        double result = 0.0;
        try {
            ps = conn.prepareStatement("SELECT X FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getX()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getDouble("X");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getX() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static double getY(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        double result = 0.0;
        try {
            ps = conn.prepareStatement("SELECT Y FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getY()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getDouble("Y");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getY() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static double getZ(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        double result = 0.0;
        try {
            ps = conn.prepareStatement("SELECT Z FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getZ()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getDouble("Z");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getZ() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String getLastSector(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            ps = conn.prepareStatement("SELECT lastsector FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getLastSector()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("lastsector");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getLastSector() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static GameMode getgm(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        GameMode result = GameMode.SURVIVAL;
        try {
            ps = conn.prepareStatement("SELECT gm FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getgm()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                String gamemodeString = rs.getString("gm");
                result = GameMode.valueOf(gamemodeString);
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getgm() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String geteffects(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            ps = conn.prepareStatement("SELECT effects FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod geteffectsr()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("effects");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z geteffects() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String getInv(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            ps = conn.prepareStatement("SELECT INV FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getInv()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("INV");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getInv() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String getEnderChest(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            ps = conn.prepareStatement("SELECT enderchest FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getEnderChest()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("enderchest");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getEnderChest() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static double gethealth(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        double result = 10;
        try {
            ps = conn.prepareStatement("SELECT health FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod gethealth()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getDouble("health");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z gethealth() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static int getfeed(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = 20;
        try {
            ps = conn.prepareStatement("SELECT feed FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getfeed()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getInt("feed");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getfeed() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String getexp(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            ps = conn.prepareStatement("SELECT exp FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getexp()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("exp");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getexp() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static int getslot(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        try {
            ps = conn.prepareStatement("SELECT slot FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getslot()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getInt("slot");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getslot() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String getArmor(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            ps = conn.prepareStatement("SELECT arrmor FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getArmor()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("arrmor");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getArmor() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String getplayerdirection(UUID u) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            ps = conn.prepareStatement("SELECT playerdirection FROM chaneelsusers WHERE uuid = ?");
            ps.setString(1, u.toString());
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod getplayerdirection()"));
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("playerdirection");
            }
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad Z getplayerdirection() :" + e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void setX(UUID u, double X) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET X = '" + X + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setX()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setX() : " + e));
        }
    }


    public static void setY(UUID u, double Y) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET Y = '" + Y + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setY()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setY() : " + e));
        }
    }
    public static void setZ(UUID u, double Z) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET Z = '" + Z + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setZ()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setZ() : " + e));
        }
    }
    public static void setinv(UUID u, String inv) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET INV = '" + inv + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setinv()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setinv() : " + e));
        }
    }
    public static void setenderchest(UUID u, String enderchest) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET enderchest = '" + enderchest + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setenderchest()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setenderchest() : " + e));
        }
    }

    public static void setlastsector(UUID u, String lastsector) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET lastsector = '" + lastsector + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setlastsector()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setlastsector() : " + e));
        }
    }
    public static void seteffects(UUID u, String effects) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET effects = '" + effects + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod seteffects()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z seteffects() : " + e));
        }
    }
    public static void setgm(UUID u, GameMode gm) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET gm = '" + gm + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setgm()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setgm() : " + e));
        }
    }
    public static void sethealth(UUID u, double health) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET health = '" + health + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod sethealth()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z sethealth() : " + e));
        }
    }

    public static void setslot(UUID u, int slot) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET slot = '" + slot + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setslot()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setslot() : " + e));
        }
    }

    public static void setArmor(UUID u, String armor) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET arrmor = '" + armor + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setArmor()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setArmor() : " + e));
        }
    }
    public static void setfeed(UUID u, double feed) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET feed = '" + feed + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setfeed()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setfeed() : " + e));
        }
    }
    public static void setexp(UUID u, String exp) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET exp = '" + exp + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setexp()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setexp() : " + e));
        }
    }
    public static void setplayerdirection(UUID u, String playerdirection) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE chaneelsusers SET playerdirection = '" + playerdirection + "' WHERE uuid = '" + u + "';");
            plugins.getLogger().info(Util.helpcolor("&a&l[W-CHANEELS] Wykonano kod setplayerdirection()"));
            ps.execute();
        } catch (SQLException e) {
            plugins.getLogger().info(Util.helpcolor("&cBlad z setplayerdirection() : " + e));
        }
    }
}
