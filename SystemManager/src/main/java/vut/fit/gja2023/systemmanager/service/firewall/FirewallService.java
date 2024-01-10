package vut.fit.gja2023.systemmanager.service.firewall;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseBadRequestException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseServerErrorException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.InvalidIPAddressException;
import vut.fit.gja2023.systemmanager.service.firewall.dto.CreateFirewallRuleDto;
import vut.fit.gja2023.systemmanager.service.firewall.dto.RemoveFirewallRuleDto;
import vut.fit.gja2023.systemmanager.util.CommandUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FirewallService {

    private static final String IP_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

    public HttpStatus createFirewallRule(CreateFirewallRuleDto dto) {
        if (!validateIP(dto.IPAddress())) {
            throw new InvalidIPAddressException();
        }
        ExitCodeEnum exitCode = CommandUtils.createHostsFileRule(dto.IPAddress());
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else {
            throw new BaseServerErrorException();
        }
    }

    public HttpStatus deleteFirewallRule(RemoveFirewallRuleDto dto) {
        if (!validateIP(dto.IPAddress())) {
            throw new InvalidIPAddressException();
        }
        ExitCodeEnum exitCode = CommandUtils.removeHostsFileRule(dto.IPAddress());
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        }
        if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new BaseBadRequestException("Firewall rule not found");
        } else {
            throw new BaseServerErrorException();
        }
    }

    private boolean validateIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            throw new BaseBadRequestException("IP Address cannot be empty");
        }
        Pattern pattern = Pattern.compile(IP_REGEX);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
