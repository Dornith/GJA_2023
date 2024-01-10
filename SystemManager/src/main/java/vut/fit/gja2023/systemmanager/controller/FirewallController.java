package vut.fit.gja2023.systemmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vut.fit.gja2023.systemmanager.service.firewall.FirewallService;
import vut.fit.gja2023.systemmanager.service.firewall.dto.CreateFirewallRuleDto;
import vut.fit.gja2023.systemmanager.service.firewall.dto.RemoveFirewallRuleDto;

import static vut.fit.gja2023.systemmanager.Constants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX + "/firewall")
@RequiredArgsConstructor
public class FirewallController {

    private final FirewallService firewallService;

    @Operation(summary = "Allow specified IP address to access the system")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Firewall rule added")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus createFirewallRule(@RequestBody @NonNull CreateFirewallRuleDto dto) {
        return firewallService.createFirewallRule(dto);
    }

    @Operation(summary = "Remove specified IP address right to access the system")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Firewall rule deleted")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Firewall rule not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus deleteFirewallRule(@RequestBody @NonNull RemoveFirewallRuleDto dto) {
        return firewallService.deleteFirewallRule(dto);
    }

}
