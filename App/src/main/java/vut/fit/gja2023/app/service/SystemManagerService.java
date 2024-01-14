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
    public ResponseEntity<HttpStatus> existsUser(String userName){
        return restTemplate.getForEntity(SYSTEM_MANAGER_USER_URL + "?user=" + userName, HttpStatus.class);
    }
    public ResponseEntity<HttpStatus> createUser(String userName, String password){
        return restTemplate.postForEntity(SYSTEM_MANAGER_USER_URL, new CreateUserDto(userName, password),HttpStatus.class);
    }
    public ResponseEntity<HttpStatus> deleteUser(String userName){
        HttpEntity<NameWrapperDto> request = new HttpEntity<>(new NameWrapperDto(userName));
        return restTemplate.exchange(SYSTEM_MANAGER_USER_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }

    // Methods to handle Groups
    public ResponseEntity<GroupInfoDto> getGroupInfo(String groupName){
        return restTemplate.getForEntity(SYSTEM_MANAGER_GROUP_URL + "?group=" + groupName, GroupInfoDto.class);
    }

    public ResponseEntity<HttpStatus> createGroup(String groupName){
        return restTemplate.postForEntity(SYSTEM_MANAGER_GROUP_URL, new NameWrapperDto(groupName),HttpStatus.class);
    }

    public ResponseEntity<HttpStatus> deleteGroup(String groupName){
        HttpEntity<NameWrapperDto> request = new HttpEntity<>(new NameWrapperDto(groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_GROUP_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }

    public ResponseEntity<HttpStatus> addUserToGroup(String userName, String groupName){
        HttpEntity<AddUserToGroupDto> request = new HttpEntity<>(new AddUserToGroupDto(userName, groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_GROUP_URL + "/user", HttpMethod.PUT, request, HttpStatus.class);
    }

    public ResponseEntity<HttpStatus> removeUserFromGroup(String userName, String groupName){
        HttpEntity<RemoveUserFromGroupDto> request = new HttpEntity<>(new RemoveUserFromGroupDto(userName, groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_GROUP_URL + "/user", HttpMethod.DELETE, request, HttpStatus.class);
    }

    // Methods to handle Directories
    public ResponseEntity<HttpStatus> createDirectory(String path, DirectoryModeEnum mode, String ownerGroupName){
        return restTemplate.postForEntity(SYSTEM_MANAGER_DIRECTORY_URL, new CreateDirectoryDto(path, mode, ownerGroupName), HttpStatus.class);
    }

    public ResponseEntity<HttpStatus> deleteDirectory(String path){
        HttpEntity<DeleteDirectoryDto> request = new HttpEntity<>(new DeleteDirectoryDto(path));
        return restTemplate.exchange(SYSTEM_MANAGER_DIRECTORY_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }

    public ResponseEntity<HttpStatus> changeDirectoryGroup(String path, String groupName){
        HttpEntity<ModifyDirectoryGroupDto> request = new HttpEntity<>(new ModifyDirectoryGroupDto(path, groupName));
        return restTemplate.exchange(SYSTEM_MANAGER_DIRECTORY_URL, HttpMethod.PUT, request, HttpStatus.class);
    }

    // Methods to handle Firewall
    public ResponseEntity<HttpStatus> createFirewallRule(String ipAddress){
        return restTemplate.postForEntity(SYSTEM_MANAGER_FIREWALL_URL, new FirewallRuleDto(ipAddress), HttpStatus.class);
    }

    public ResponseEntity<HttpStatus> removeFirewallRule(String ipAddress){
        HttpEntity<FirewallRuleDto> request = new HttpEntity<>(new FirewallRuleDto(ipAddress));
        return restTemplate.exchange(SYSTEM_MANAGER_FIREWALL_URL, HttpMethod.DELETE, request, HttpStatus.class);
    }

}
