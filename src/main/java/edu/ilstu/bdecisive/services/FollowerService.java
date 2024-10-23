package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.FollowerRequestDTO;
import edu.ilstu.bdecisive.models.Follower;
import edu.ilstu.bdecisive.utils.ServiceException;

public interface FollowerService {
    Follower createFollower(FollowerRequestDTO requestDTO) throws ServiceException;
}
