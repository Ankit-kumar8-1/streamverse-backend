package in.ankit_Saahariya.stream_verse.service;

import in.ankit_Saahariya.stream_verse.dto.request.UserRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.UserResponse;

public interface UserService {
    MessageResponse createUser(UserRequest userRequest);

    MessageResponse updateUser(Long id, UserRequest userRequest);


    PageResponse<UserResponse> getUsers(int page, int size, String search);

    MessageResponse deleteUser(Long id, String currentUserEmail);
}
