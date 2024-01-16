package vut.fit.gja2023.app.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vut.fit.gja2023.app.dto.*;
import vut.fit.gja2023.app.enums.DirectoryModeEnum;

/**
 * A service used for communicating with a REST API of a system manager.
 */
@Service
@RequiredArgsConstructor
public class SystemManagerService {

    @Value("${systemManager.url.user}")
    private  String SYSTEM_MANAGER_USER_URL;
    @Value("${systemManager.url.group}")
    private  String SYSTEM_MANAGER_GROUP_URL;
    @Value("${systemManager.url.firewall}")
    private  String SYSTEM_MANAGER_FIREWALL_URL;
    @Value("${systemManager.url.directory}")
    private  String SYSTEM_MANAGER_DIRECTORY_URL;

    private final RestTemplate restTemplate;

    // Methods to handle User
    /**
     * Checks whether a specified user exists in the system.
     * 
     * @param userLogin The login of the user.
     * @return HTTP response indicating whether a user exits or not, or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> existsUser(String userLogin){
        return restTemplate.getForEntity(SYSTEM_MANAGER_USER_URL + "?user=" + userLogin, HttpStatus.class);
    }
    /**
     * Tries to create a new user in the system.
     * 
     * @param userLogin The login of the user.
     * @param password The user's password.
     * @return HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> createUser(String userLogin, String password){
        return restTemplate.postForEntity(SYSTEM_MANAGER_USER_URL, new CreateUserDto(userLogin, password),HttpStatus.class);
    }
    /**
     * Tries to delete a specified user.
     * 
     * @param userLogin The login of the user.
     * @return HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> deleteUser(String userLogin){
        HttpEntity<NameWrapperDto> request = new HttpEntity<>(new NameWrapperDto(userLogin));
        return restTemplate.exchange(SYSTEM_MANAGER_USER_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }

    // Methods to handle Groups
    /**
     * Gets information about a user group from the system.
     * 
     * @param groupName
     * @return HTTP response containing group information or a problem that ocurred.
     */
    public ResponseEntity<GroupInfoDto> getGroupInfo(String groupName){
        return restTemplate.getForEntity(SYSTEM_MANAGER_GROUP_URL + "?group=" + groupName, GroupInfoDto.class);
    }
    /**
     * Tries to create a new user group in the system.
     * 
     * @param groupName The name of the group.
     * @return HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> createGroup(String groupName){
        return restTemplate.postForEntity(SYSTEM_MANAGER_GROUP_URL, new NameWrapperDto(groupName),HttpStatus.class);
    }
    /**
     * Tries to delete a specified user group.
     * 
     * @param groupName The name of the group.
     * @return  HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> deleteGroup(String groupName){
        HttpEntity<NameWrapperDto> request = new HttpEntity<>(new NameWrapperDto(groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_GROUP_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }
    /**
     * Tries to add a specified user to a chosen user group.
     * 
     * @param userLogin A user's login.
     * @param groupName The name of the group.
     * @return HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> addUserToGroup(String userLogin, String groupName){
        HttpEntity<AddUserToGroupDto> request = new HttpEntity<>(new AddUserToGroupDto(userLogin, groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_GROUP_URL + "/user", HttpMethod.PUT, request, HttpStatus.class);
    }
    /**
     * Tries to remove a specified user from a chosen user group.
     * 
     * @param userLogin A user's login.
     * @param groupName The name of the user.
     * @return HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> removeUserFromGroup(String userLogin, String groupName){
        HttpEntity<RemoveUserFromGroupDto> request = new HttpEntity<>(new RemoveUserFromGroupDto(userLogin, groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_GROUP_URL + "/user", HttpMethod.DELETE, request, HttpStatus.class);
    }

    // Methods to handle Directories
    /**
     * Tries to create a new directory in the system.
     * 
     * @param path The path of the new directory.
     * @param mode A directory mode representing permissions.
     * @param ownerGroupName The name of the group this directory belongs to.
     * @return HTTP response indicating success or a problem that ocurred. 
     */
    public ResponseEntity<HttpStatus> createDirectory(String path, DirectoryModeEnum mode, String ownerGroupName){
        return restTemplate.postForEntity(SYSTEM_MANAGER_DIRECTORY_URL, new CreateDirectoryDto(path, mode, ownerGroupName), HttpStatus.class);
    }
    /**
     * Tries to delete a specified directory in the system.
     * 
     * @param path The path of the directory.
     * @return HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> deleteDirectory(String path){
        HttpEntity<DeleteDirectoryDto> request = new HttpEntity<>(new DeleteDirectoryDto(path));
        return restTemplate.exchange(SYSTEM_MANAGER_DIRECTORY_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }
    /**
     * Changes which group the specified directory belongs to.
     * 
     * @param path The path of the directory.
     * @param groupName The name of the group.
     * @return HTTP response indicating success or a problem that ocurred. 
     */
    public ResponseEntity<HttpStatus> changeDirectoryGroup(String path, String groupName){
        HttpEntity<ModifyDirectoryGroupDto> request = new HttpEntity<>(new ModifyDirectoryGroupDto(path, groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_DIRECTORY_URL, HttpMethod.PUT, request, HttpStatus.class);
    }

    // Methods to handle Firewall
    public ResponseEntity<HttpStatus> createFirewallRule(String ipAddress){
        return restTemplate.postForEntity(SYSTEM_MANAGER_FIREWALL_URL, new FirewallRuleDto(ipAddress), HttpStatus.class);
    }
    /**
     * Tries to create a firewall rule in the system.
     * 
     * @param ipAddress The IP address that will be permited to enter the system.
     * @return HTTP response indicating success or a problem that ocurred.
     */
    public ResponseEntity<HttpStatus> removeFirewallRule(String ipAddress){
        HttpEntity<FirewallRuleDto> request = new HttpEntity<>(new FirewallRuleDto(ipAddress));
        return restTemplate.exchange(SYSTEM_MANAGER_FIREWALL_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }

}
