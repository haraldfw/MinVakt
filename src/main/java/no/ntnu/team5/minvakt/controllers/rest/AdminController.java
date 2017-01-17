package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.SwapInfo;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alan on 16/01/2017.
 */

@RestController
public class AdminController {
    @Autowired
    AccessContextFactory accessor;

    @Authorize
    @PostMapping
    public void acceptSwap(Verifier verifier, @ModelAttribute SwapInfo swapInfo) {
        verifier.ensure(Verifier.hasRole("admin"));

        accessor.with(access -> {
            boolean success = access.notification.verify(swapInfo.getNotificationId(), swapInfo);

            if (success) {
                Shift shift = access.shift.getShiftFromId(swapInfo.getShiftId());
                User user = access.user.fromUsername(swapInfo.getUsername());
                shift.setUser(user);
            } else {
                //FIXME
            }
        });
    }
}
