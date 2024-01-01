package com.vytsablinskas.flavorfare.business.services.impl;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.interfaces.ReservationService;
import com.vytsablinskas.flavorfare.database.domain.ReservationEntity;
import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import com.vytsablinskas.flavorfare.database.repositories.ReservationRepository;
import com.vytsablinskas.flavorfare.database.repositories.RestaurantRepository;
import com.vytsablinskas.flavorfare.database.repositories.TableRepository;
import com.vytsablinskas.flavorfare.shared.constants.Messages;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.AddReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.ReservationDto;
import com.vytsablinskas.flavorfare.shared.dtos.reservation.UpdateReservationDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    private final RestaurantRepository restaurantRepository;

    private final TableRepository tableRepository;

    private final ModelMapper modelMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RestaurantRepository restaurantRepository,
                                  TableRepository tableRepository,
                                  ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.tableRepository = tableRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReservationDto> getRestaurantReservations(Integer restaurantId) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findById(restaurantId);
        if (optionalRestaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        List<ReservationEntity> reservationsInRestaurant = reservationRepository.findByRestaurantId(optionalRestaurantEntity.get().getRestaurantId());

        return reservationsInRestaurant
                .stream()
                .map(entity -> modelMapper.map(entity, ReservationDto.class))
                .toList();
    }

    @Override
    public ReservationDto addReservation(Integer restaurantId, Integer tableId, AddReservationDto addReservationDto) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findById(restaurantId);
        if (optionalRestaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        Optional<TableEntity> optionalTableEntity = tableRepository.findById(tableId);
        if (optionalTableEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getTableNotFoundMessage(tableId));
        }

        ReservationEntity reservationEntityToAdd = modelMapper.map(addReservationDto, ReservationEntity.class);
        reservationEntityToAdd.setTable(optionalTableEntity.get());
        ReservationEntity createdEntity = reservationRepository.save(reservationEntityToAdd);

        return modelMapper.map(createdEntity, ReservationDto.class);
    }

    @Override
    public ReservationDto updateReservation(Integer restaurantId,
                                            Integer tableId,
                                            Integer reservationId,
                                            UpdateReservationDto updateReservationDto) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findById(restaurantId);
        if (optionalRestaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        Optional<TableEntity> optionalTableEntity = tableRepository.findById(tableId);
        if (optionalTableEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getTableNotFoundMessage(tableId));
        }

        Optional<ReservationEntity> optionalReservationEntity = reservationRepository.findById(reservationId);
        if (optionalReservationEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getReservationNotFoundMessage(reservationId));
        }

        ReservationEntity reservationEntity = optionalReservationEntity.get();
        modelMapper.map(updateReservationDto, reservationEntity);
        ReservationEntity updatedReservation = reservationRepository.save(reservationEntity);

        return modelMapper.map(updatedReservation, ReservationDto.class);
    }

    @Override
    public void deleteReservation(Integer restaurantId, Integer tableId, Integer reservationId) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findById(restaurantId);
        if (optionalRestaurantEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getRestaurantNotFoundMessage(restaurantId));
        }

        Optional<TableEntity> optionalTableEntity = tableRepository.findById(tableId);
        if (optionalTableEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getTableNotFoundMessage(tableId));
        }

        Optional<ReservationEntity> optionalReservationEntity = reservationRepository.findById(reservationId);
        if (optionalReservationEntity.isEmpty()) {
            throw new ResourceNotFoundException(Messages.getReservationNotFoundMessage(reservationId));
        }

        reservationRepository.deleteById(reservationId);
    }
}
