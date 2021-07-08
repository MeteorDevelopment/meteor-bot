package meteordevelopment.meteorbot.tickets;

import static meteordevelopment.meteorbot.Utils.embed;

public class Problem {
    public final String message;
    public final String solution;

    public Problem(String message, String solution) {
        this.message = message;
        this.solution = solution;
    }

    public void sendSolution(Ticket ticket) {
        ticket.channel.sendMessage(embed(solution).setFooter("React with ✅ to close this ticket.").build()).queue(message -> {
            message.addReaction("✅").queue();
            ticket.setMessage(message);
        });
    }
}