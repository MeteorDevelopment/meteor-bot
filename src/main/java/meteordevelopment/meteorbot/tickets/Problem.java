package meteordevelopment.meteorbot.tickets;

import static meteordevelopment.meteorbot.utils.Utils.embed;

public class Problem {
    public final String msg;
    public final String solution;

    public Problem(String msg, String solution) {
        this.msg = msg;
        this.solution = solution;
    }

    public void sendSolution(Ticket ticket) {
        ticket.channel.sendMessage(embed(solution).setFooter("React with ✅ to close this ticket and ❌ to speak to the helpers.").build()).queue(message -> {
            message.addReaction("✅").queue();
            message.addReaction("❌").queue();

            ticket.setMessage(message);
        });
    }
}
