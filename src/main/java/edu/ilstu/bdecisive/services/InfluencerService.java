package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.InfluencerDTO;
import edu.ilstu.bdecisive.utils.ServiceException;

public interface InfluencerService {
    void create(InfluencerDTO influencerDTO) throws ServiceException;
}
