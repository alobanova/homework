package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class SerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    /**
     * Конструктор не меняйте.
     */
    public SerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "File name is not to be null");
        Objects.requireNonNull(gameState, "Game state is not to be null");

        String path = filesDirectory + "/" + filename;

        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {

            outputStream.writeObject(gameState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("The path was not found", e, SaveGameException.Type.SYSTEM, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Fail or interrupt I/O operations", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "File name is not to be null");

        String path = filesDirectory + "/" + filename;

        MapState<GameObject> gameState = null;

        try (FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {

            gameState = (MapState<GameObject>) inputStream.readObject();
            return gameState;

        } catch (FileNotFoundException e) {
            throw new SaveGameException("The file was not found", e, SaveGameException.Type.SYSTEM, gameState);
        } catch (ClassNotFoundException e) {
            throw new SaveGameException("Class of a serialized object cannot be found", e,
                    SaveGameException.Type.SYSTEM, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Fail or interrupt I/O operations", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                   InstantiatableEntity.Status status,
                                   long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }

}
