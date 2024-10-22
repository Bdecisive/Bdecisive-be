package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.models.Follower;

public interface FollowerService {
    Follower createFollower(Follower follower);
    String addComment(Long followerId, String comment);
}
