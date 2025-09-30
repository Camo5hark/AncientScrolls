package com.andrewreedhall.ancientscrolls.command.handlers;

import com.andrewreedhall.ancientscrolls.command.AncientScrollsCommand;
import com.andrewreedhall.ancientscrolls.command.CommandHandler;

public final class CommandHandler_asgive extends CommandHandler {
    public CommandHandler_asgive() {
        super("asgive");
    }

    @Override
    public void validate(final AncientScrollsCommand command) {
        this.validateOnlinePlayerNameArg(command, 0);
    }

    @Override
    public boolean execute(final AncientScrollsCommand command) {
        return true;
    }
}
