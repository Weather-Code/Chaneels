package weathercode.chaneels.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import weathercode.chaneels.main.Main;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String helpcolor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static String serializeInventory(ItemStack[] inventory) {
        JSONArray jsonInventory = new JSONArray();

        for (ItemStack item : inventory) {
            JSONObject jsonItem = new JSONObject();

            if (item == null || item.getType() == Material.AIR) {
                jsonItem.put("type", "null");
            } else {
                jsonItem.put("type", item.getType().name());
                jsonItem.put("amount", item.getAmount());
                jsonItem.put("durability", item.getDurability());

                if (item.getEnchantments().size() > 0) {
                    JSONObject jsonEnchants = new JSONObject();
                    for (Enchantment enchantment : item.getEnchantments().keySet()) {
                        int level = item.getEnchantments().get(enchantment);
                        jsonEnchants.put(enchantment.getName(), level);
                    }
                    jsonItem.put("enchants", jsonEnchants);
                }
            }

            jsonInventory.add(jsonItem);
        }

        return jsonInventory.toJSONString();
    }
    public static String serializeArmor(ItemStack[] armor) {
        JSONArray jsonInventory = new JSONArray();

        for (ItemStack item : armor) {
            JSONObject jsonItem = new JSONObject();

            if (item == null || item.getType() == Material.AIR) {
                jsonItem.put("armor_type", "null");
            } else {
                jsonItem.put("armor_type", item.getType().name());
                jsonItem.put("amount", item.getAmount());
                jsonItem.put("durability", item.getDurability());

                if (item.getEnchantments().size() > 0) {
                    JSONObject jsonEnchants = new JSONObject();
                    for (Enchantment enchantment : item.getEnchantments().keySet()) {
                        int level = item.getEnchantments().get(enchantment);
                        jsonEnchants.put(enchantment.getName(), level);
                    }
                    jsonItem.put("enchants", jsonEnchants);
                }
            }

            jsonInventory.add(jsonItem);
        }

        return jsonInventory.toJSONString();
    }


    public static ItemStack[] deserializeInventory(String serialized) {
        JSONArray jsonInventory;
        try {
            jsonInventory = (JSONArray) new JSONParser().parse(serialized);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ItemStack[0];
        }

        ItemStack[] inventory = new ItemStack[jsonInventory.size()];

        for (int i = 0; i < jsonInventory.size(); i++) {
            JSONObject jsonItem = (JSONObject) jsonInventory.get(i);
            String type = (String) jsonItem.get("type");
            Material material = Material.matchMaterial(type);

            if (material != null) {
                int amount = ((Long) jsonItem.get("amount")).intValue();
                short durability = ((Long) jsonItem.get("durability")).shortValue();
                ItemStack item = new ItemStack(material, amount, durability);

                if (jsonItem.containsKey("enchants")) {
                    JSONObject jsonEnchants = (JSONObject) jsonItem.get("enchants");
                    for (Object enchantObj : jsonEnchants.keySet()) {
                        String enchantName = (String) enchantObj;
                        int level = ((Long) jsonEnchants.get(enchantName)).intValue();
                        Enchantment enchantment = Enchantment.getByName(enchantName);
                        if (enchantment != null) {
                            item.addUnsafeEnchantment(enchantment, level);
                        }
                    }
                }

                inventory[i] = item;
            }
        }

        return inventory;
    }
    public static ItemStack[] deserializeArmor(String serialized) {
        JSONArray jsonArmor;
        try {
            jsonArmor = (JSONArray) new JSONParser().parse(serialized);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ItemStack[0];
        }

        ItemStack[] armor = new ItemStack[jsonArmor.size()];

        for (int i = 0; i < jsonArmor.size(); i++) {
            JSONObject jsonItem = (JSONObject) jsonArmor.get(i);
            String armorType = (String) jsonItem.get("armor_type");
            Material material = Material.matchMaterial(armorType);

            if (material != null) {
                int amount = ((Long) jsonItem.get("amount")).intValue();
                short durability = ((Long) jsonItem.get("durability")).shortValue();
                ItemStack item = new ItemStack(material, amount, durability);

                if (jsonItem.containsKey("enchants")) {
                    JSONObject jsonEnchants = (JSONObject) jsonItem.get("enchants");
                    for (Object enchantObj : jsonEnchants.keySet()) {
                        String enchantName = (String) enchantObj;
                        int level = ((Long) jsonEnchants.get(enchantName)).intValue();
                        Enchantment enchantment = Enchantment.getByName(enchantName);
                        if (enchantment != null) {
                            item.addUnsafeEnchantment(enchantment, level);
                        }
                    }
                }

                armor[i] = item;
            }
        }

        return armor;
    }
    public static String serializeEffects(Player player) {
        JSONArray jsonEffects = new JSONArray();

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            JSONObject jsonEffect = new JSONObject();
            PotionEffectType effectType = potionEffect.getType();

            jsonEffect.put("type", effectType.getName());
            jsonEffect.put("duration", potionEffect.getDuration());
            jsonEffect.put("amplifier", potionEffect.getAmplifier());

            jsonEffects.add(jsonEffect);
        }

        return jsonEffects.toJSONString();
    }

    public static void deserializeEffects(Player player, String serialized) {
        try {
            JSONArray jsonEffects = (JSONArray) new JSONParser().parse(serialized);

            for (Object effectObj : jsonEffects) {
                if (effectObj instanceof JSONObject) {
                    JSONObject jsonEffect = (JSONObject) effectObj;
                    String type = (String) jsonEffect.get("type");
                    int duration = ((Long) jsonEffect.get("duration")).intValue();
                    int amplifier = ((Long) jsonEffect.get("amplifier")).intValue();

                    PotionEffectType effectType = PotionEffectType.getByName(type);

                    if (effectType != null) {
                        PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier);
                        player.addPotionEffect(potionEffect);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String serializeDirection(Player player) {
        JSONArray jsondirects = new JSONArray();
        JSONObject jsondirect = new JSONObject();

        jsondirect.put("pitch", + player.getLocation().getPitch());
        jsondirect.put("yaw", + player.getLocation().getYaw());
        jsondirects.add(jsondirect);

        return jsondirects.toJSONString();

    }

    public static void deserializeDirection(Player player, Location loc, String serialized) {
        try {
            JSONArray jsondirects = (JSONArray) new JSONParser().parse(serialized);

            for (Object effectObj : jsondirects) {
                if (effectObj instanceof JSONObject) {
                    JSONObject jsonEffect = (JSONObject) effectObj;
                    Double pitch = (Double) jsonEffect.get("pitch");
                    Double yaw = (Double) jsonEffect.get("yaw");
                    loc.setYaw(yaw.floatValue());
                    loc.setPitch(pitch.floatValue());
                    player.teleport(loc);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String serializeexp(Player plr) {
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("exp", + plr.getExp());
        jsonObject.put("explevel", + plr.getLevel());
        jsonArray.add(jsonObject);

        return jsonArray.toJSONString();
    }

    public static void deserializeexp(Player player, String serialized) {
        try {
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(serialized);

            for (Object desrObj : jsonArray) {
                if (desrObj instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) desrObj;
                    Double exp = (Double) jsonObject.get("exp");
                    int explevel = ((Long) jsonObject.get("explevel")).intValue();
                    player.setExp(exp.floatValue());
                    player.setLevel(explevel);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public static void deserializechaneels(String serialized, Inventory inv) {
        try {
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(serialized);
            for (Object desrObj : jsonArray) {
                if (desrObj instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) desrObj;
                    String name = (String) jsonObject.get("name");
                    String players = (String) jsonObject.get("players");
                    String status = (String) jsonObject.get("status");
                    String tps = (String) jsonObject.get("tps");
                    if(!status.equals("OFFLINE")) {
                        if(name.equals(Main.getInstance().getConfig().getString("ActualChaneel.ChaneelID"))) {
                            ItemStack chaneel = new ItemStack(Material.CHEST_MINECART);
                            ItemMeta meta = chaneel.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            meta.setDisplayName(Util.helpcolor("&6&l" + name));
                            lore.add("");
                            lore.add(Util.helpcolor("&7⨯ &6&lTPS&7&l: &f&l" + tps + " &7⨯"));
                            lore.add(Util.helpcolor("&7⨯ &6&lGraczy&7: &f&l" + players + " &7⨯"));
                            lore.add(" ");
                            lore.add(Util.helpcolor("&7⨯ &eKLIKNIJ BY DOLACZYC NA KANAL &7⨯"));
                            lore.add("");
                            lore.add("");
                            lore.add(Util.helpcolor("&a&lAKTUALNIE ZNAJDUJESZ SIE NA TYM KANALE"));
                            meta.setLore(lore);
                            chaneel.setItemMeta(meta);
                            inv.addItem(chaneel);
                        } else {
                            ItemStack chaneel = new ItemStack(Material.MINECART);
                            ItemMeta meta = chaneel.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            meta.setDisplayName(Util.helpcolor("&6&l" + name));
                            lore.add("");
                            lore.add(Util.helpcolor("&7⨯ &6&lTPS&7&l: &f&l" + tps + " &7⨯"));
                            lore.add(Util.helpcolor("&7⨯ &6&lGraczy&7: &f&l" + players + " &7⨯"));
                            lore.add(" ");
                            lore.add(Util.helpcolor("&7⨯ &eKLIKNIJ BY DOLACZYC NA KANAL &7⨯"));
                            meta.setLore(lore);
                            chaneel.setItemMeta(meta);
                            inv.addItem(chaneel);
                        }
                    }
                    }
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
