package space.wolv.lanterns.API.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class Hash
{
    private final static HashMap<String, ArrayList<Location>> lanternLocations = new HashMap<>();
    private final static HashMap<String, Boolean> hBool = new HashMap<>();

    public static ArrayList<Location> getLocations(String playerUUID)
    {
        if (lanternLocations.containsKey(playerUUID))
        {
            return lanternLocations.get(playerUUID);
        }
        else
        {
            return new ArrayList<>();
        }
    }

    public static boolean getBool(String playerUUID, DataKeys key)
    {
        if (hBool.containsKey(playerUUID + "." + key))
        {
            return hBool.get(playerUUID + "." + key);
        }
        else
        {
            return false;
        }
    }

    public static void addLocation(String playerUUID, Location location)
    {
        ArrayList<Location> locations;

        if (lanternLocations.containsKey(playerUUID))
        {
            locations = lanternLocations.get(playerUUID);
        }
        else
        {
            locations = new ArrayList<>();
        }

        locations.add(location);
        lanternLocations.put(playerUUID, locations);
    }

    public static void setBool(String playerUUID, DataKeys key, Boolean value)
    {
        hBool.put(playerUUID + "." + key, value);
    }

    public static boolean removeLocation(String playerUUID, Location location)
    {
        if (lanternLocations.containsKey(playerUUID))
        {
            ArrayList<Location> locations = lanternLocations.get(playerUUID);
            locations.remove(location);
            lanternLocations.put(playerUUID, locations);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean removeBool(String playerUUID, DataKeys key)
    {
        if (hBool.containsKey(playerUUID + "." + key))
        {
            hBool.remove(playerUUID + "." + key);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean hasLocations(String playerUUID)
    {
        if (lanternLocations.containsKey(playerUUID))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean hasBool(String playerUUID, DataKeys key)
    {
        if (hBool.containsKey(playerUUID + "." + key))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
