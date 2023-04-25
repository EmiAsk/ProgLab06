package se.ifmo.lab06.server.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.ifmo.lab06.shared.model.Flat;
import se.ifmo.lab06.shared.model.Furnish;
import se.ifmo.lab06.server.parser.JsonParser;
import se.ifmo.lab06.shared.util.ZonedDateTimeSerializer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class CollectionManager {
    private final Stack<Flat> collection;
    private final ZonedDateTime createdAt = ZonedDateTime.now();
    private final String fileName;

    private CollectionManager(String fileName) {
        this.collection = new Stack<>();
        this.fileName = fileName;
    }

    public List<Flat> getCollection() {
        return collection;
    }


    public static CollectionManager fromFile(String fileName) throws FileNotFoundException {
        CollectionManager collection = new CollectionManager(fileName);
        Flat[] flats = JsonParser.parseFile(new FileReader(fileName));
        for (Flat flat : flats) collection.push(flat);
        return collection;
    }

    public void push(Flat element) {
        if (element.validate() && get(element.getId()) == null) {
            collection.push(element);
        }
    }

    public void update(long id, Flat newFlat) {
        Flat flat = get(id);
        flat.update(newFlat);
    }

    public void removeById(long id) {
        collection.removeElement(get(id));
    }

    public void clear() {
        collection.clear();
    }

    public void pop() {
        if (!collection.isEmpty()) collection.pop();
    }

    public Flat get(long id) {
        for (Flat flat : collection) if (flat.getId() == id) return flat;
        return null;
    }

    public String description() {
        return String.format("Тип: %s\nДата инициализации: %s\nКол-во элементов: %s",
                collection.getClass().getName(), createdAt, collection.size());
    }

    public String getFileName() {
        return fileName;
    }

    public String dump() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer());
        Gson gson = builder.create();
        return gson.toJson(collection.toArray(), Flat[].class);
    }

    public boolean validate() {
        HashSet<Long> idSet = new HashSet<>();
        for (Flat flat : collection) {
            if (!flat.validate()) {
                return false;
            }
            idSet.add(flat.getId());
        }
        return (idSet.size() == collection.size());
    }

    public Flat min() {
        return !collection.isEmpty() ? collection.stream()
                .min(Flat::compareTo)
                .get() : null;
    }

    public void shuffle() {
        Collections.shuffle(collection);
    }

    public long removeByFurnish(Furnish furnish) {
        long n = collection
                .stream()
                .filter(flat -> flat.getFurnish() == furnish)
                .count();
        collection.removeIf(flat -> flat.getFurnish() == furnish);
        return n;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Flat flat : collection) {
            builder.append(flat);
            builder.append("\n");
        }
        return builder.toString();
    }
}
