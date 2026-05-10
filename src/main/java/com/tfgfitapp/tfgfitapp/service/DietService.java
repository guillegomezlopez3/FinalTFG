package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.*;
import com.tfgfitapp.tfgfitapp.entity.*;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.DietMealRepository;
import com.tfgfitapp.tfgfitapp.repository.DietRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de Dietas y Comidas.
 * 
 * Proporciona la lógica para crear planes nutricionales, gestionar las comidas
 * diarias y controlar el seguimiento de las mismas por parte de los clientes.
 */
@Service
public class DietService {

    public DietService(DietRepository dietRepository, DietMealRepository dietMealRepository,
                       ClientRepository clientRepository, TrainerRepository trainerRepository) {
        this.dietRepository = dietRepository;
        this.dietMealRepository = dietMealRepository;
        this.clientRepository = clientRepository;
        this.trainerRepository = trainerRepository;
    }

    private final DietRepository dietRepository;
    private final DietMealRepository dietMealRepository;
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;

    // ===== DIETAS =====

    /**
     * Crea una nueva dieta asignada a un cliente.
     * 
     * @param request Datos de la dieta.
     * @param currentUser Entrenador que crea la dieta.
     * @return Respuesta con los datos de la dieta guardada.
     */
    @Transactional
    public DietResponse createDiet(DietRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Client client = getClientOrThrow(request.getClientId());
        ensureClientBelongsToTrainer(client, trainer);

        Diet diet = new Diet();
        diet.setTrainer(trainer);
        diet.setClient(client);
        diet.setTitle(request.getTitle());
        diet.setDescription(request.getDescription());
        diet.setStartDate(request.getStartDate());
        diet.setEndDate(request.getEndDate());
        diet.setActive(request.getActive() != null ? request.getActive() : true);

        return toResponse(dietRepository.save(diet));
    }

    /**
     * Obtiene todas las dietas asociadas a un cliente específico.
     * 
     * @param clientId ID del cliente.
     * @param currentUser Usuario que realiza la consulta.
     * @return Lista de respuestas con las dietas del cliente.
     */
    @Transactional(readOnly = true)
    public List<DietResponse> getDietsByClient(Long clientId, User currentUser) {
        Client client = getClientOrThrow(clientId);
        checkDietReadAccess(client, currentUser);
        return dietRepository.findAllByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los detalles de una dieta específica por su ID.
     * 
     * @param dietId ID de la dieta.
     * @param currentUser Usuario que realiza la consulta.
     * @return Respuesta con los datos de la dieta.
     */
    @Transactional(readOnly = true)
    public DietResponse getDietById(Long dietId, User currentUser) {
        Diet diet = findDietOrThrow(dietId);
        checkSingleDietReadAccess(diet, currentUser);
        return toResponse(diet);
    }

    /**
     * TRAINER actualiza una dieta propia.
     */
    @Transactional
    public DietResponse updateDiet(Long dietId, DietRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Diet diet = dietRepository.findByIdAndTrainerId(dietId, trainer.getId())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a esta dieta"));

        if (request.getTitle() != null)       diet.setTitle(request.getTitle());
        if (request.getDescription() != null) diet.setDescription(request.getDescription());
        if (request.getStartDate() != null)   diet.setStartDate(request.getStartDate());
        if (request.getEndDate() != null)     diet.setEndDate(request.getEndDate());
        if (request.getActive() != null)      diet.setActive(request.getActive());

        return toResponse(dietRepository.save(diet));
    }

    /**
     * TRAINER elimina una dieta propia.
     */
    @Transactional
    public void deleteDiet(Long dietId, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Diet diet = dietRepository.findByIdAndTrainerId(dietId, trainer.getId())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a esta dieta"));
        dietRepository.delete(diet);
    }

    // ===== COMIDAS (DietMeal) =====

    /**
     * TRAINER añade una comida a una dieta propia.
     */
    @Transactional
    public DietMealResponse addMeal(Long dietId, DietMealRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Diet diet = dietRepository.findByIdAndTrainerId(dietId, trainer.getId())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a esta dieta"));

        DietMeal meal = new DietMeal();
        meal.setDiet(diet);
        meal.setMealType(request.getMealType());
        meal.setMealTime(request.getMealTime());
        meal.setFoods(request.getFoods());
        meal.setCalories(request.getCalories());
        meal.setNotes(request.getNotes());
        meal.setProtein(request.getProtein());
        meal.setCarbs(request.getCarbs());
        meal.setFats(request.getFats());
        meal.setCompleted(request.getCompleted() != null ? request.getCompleted() : false);

        return toMealResponse(dietMealRepository.save(meal));
    }

    /**
     * TRAINER actualiza una comida de una dieta propia.
     */
    @Transactional
    public DietMealResponse updateMeal(Long mealId, DietMealRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        DietMeal meal = getMealOrThrow(mealId);

        if (!meal.getDiet().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a esta comida");
        }

        if (request.getMealType() != null) meal.setMealType(request.getMealType());
        if (request.getMealTime() != null) meal.setMealTime(request.getMealTime());
        if (request.getFoods() != null)    meal.setFoods(request.getFoods());
        if (request.getCalories() != null) meal.setCalories(request.getCalories());
        if (request.getNotes() != null)    meal.setNotes(request.getNotes());
        if (request.getProtein() != null)  meal.setProtein(request.getProtein());
        if (request.getCarbs() != null)    meal.setCarbs(request.getCarbs());
        if (request.getFats() != null)     meal.setFats(request.getFats());
        if (request.getCompleted() != null) meal.setCompleted(request.getCompleted());

        return toMealResponse(dietMealRepository.save(meal));
    }

    /**
     * CLIENT marca una comida como completada/pendiente.
     */
    @Transactional
    public DietMealResponse toggleMealCompletion(Long mealId, User currentUser) {
        DietMeal meal = getMealOrThrow(mealId);
        if (!meal.getDiet().getClient().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes acceso a esta comida");
        }
        meal.setCompleted(meal.getCompleted() == null ? true : !meal.getCompleted());
        return toMealResponse(dietMealRepository.save(meal));
    }

    /**
     * TRAINER elimina una comida de una dieta propia.
     */
    @Transactional
    public void deleteMeal(Long mealId, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        DietMeal meal = getMealOrThrow(mealId);

        if (!meal.getDiet().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a esta comida");
        }
        dietMealRepository.delete(meal);
    }

    // ===== HELPERS DE ACCESO =====

    private void checkDietReadAccess(Client client, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getRole() == Role.TRAINER) {
            Trainer trainer = getTrainerOrThrow(currentUser);
            if (client.getTrainer() == null || !client.getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("Este cliente no es tuyo");
            }
        } else if (currentUser.getRole() == Role.CLIENT) {
            if (!client.getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes ver tus propias dietas");
            }
        }
    }

    private void checkSingleDietReadAccess(Diet diet, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getRole() == Role.TRAINER) {
            Trainer trainer = getTrainerOrThrow(currentUser);
            if (!diet.getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("No tienes acceso a esta dieta");
            }
        } else if (currentUser.getRole() == Role.CLIENT) {
            if (!diet.getClient().getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes ver tus propias dietas");
            }
        }
    }

    private void ensureClientBelongsToTrainer(Client client, Trainer trainer) {
        if (client.getTrainer() == null || !client.getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("Este cliente no está asignado a ti");
        }
    }

    // ===== LOOKUPS =====

    private Diet findDietOrThrow(Long id) {
        return dietRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dieta no encontrada con ID: " + id));
    }

    private DietMeal getMealOrThrow(Long id) {
        return dietMealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida no encontrada con ID: " + id));
    }

    private Client getClientOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
    }

    private Trainer getTrainerOrThrow(User user) {
        return trainerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
    }

    // ===== MAPPERS =====

    public DietResponse toResponse(Diet diet) {
        List<DietMealResponse> mealsList = diet.getMeals() != null
                ? diet.getMeals().stream().map(this::toMealResponse).collect(Collectors.toList())
                : List.of();

        DietResponse response = new DietResponse();
        response.setId(diet.getId());
        response.setClientId(diet.getClient().getId());
        response.setClientName(diet.getClient().getUser() != null ? diet.getClient().getUser().getName() : null);
        response.setTrainerId(diet.getTrainer().getId());
        response.setTrainerName(diet.getTrainer().getUser() != null ? diet.getTrainer().getUser().getName() : null);
        response.setTitle(diet.getTitle());
        response.setDescription(diet.getDescription());
        response.setStartDate(diet.getStartDate());
        response.setEndDate(diet.getEndDate());
        response.setActive(diet.getActive());
        response.setCreatedAt(diet.getCreatedAt());
        response.setUpdatedAt(diet.getUpdatedAt());
        response.setMeals(mealsList);
        return response;
    }

    public DietMealResponse toMealResponse(DietMeal meal) {
        DietMealResponse response = new DietMealResponse();
        response.setId(meal.getId());
        response.setDietId(meal.getDiet() != null ? meal.getDiet().getId() : null);
        response.setMealType(meal.getMealType());
        response.setMealTime(meal.getMealTime());
        response.setFoods(meal.getFoods());
        response.setCalories(meal.getCalories());
        response.setNotes(meal.getNotes());
        response.setProtein(meal.getProtein());
        response.setCarbs(meal.getCarbs());
        response.setFats(meal.getFats());
        response.setCompleted(meal.getCompleted());
        return response;
    }
}

