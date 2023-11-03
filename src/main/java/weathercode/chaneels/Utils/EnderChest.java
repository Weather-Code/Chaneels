package weathercode.chaneels.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EnderChest {
    public static String serializeEc(ItemStack[] inventory) {
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

    public static ItemStack[] deserializeEc(String serialized) {
        JSONArray jsonInventory;
        try {
            if (serialized != null && !serialized.isEmpty()) {
                jsonInventory = (JSONArray) new JSONParser().parse(serialized);
            } else {
                return new ItemStack[0]; // Brak danych, zwróć pustą tablicę
            }
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
                int durability = ((Long) jsonItem.get("durability")).intValue();
                ItemStack item = new ItemStack(material, amount, (short) durability);

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
}
