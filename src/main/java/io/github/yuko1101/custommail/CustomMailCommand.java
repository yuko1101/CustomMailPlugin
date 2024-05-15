package io.github.yuko1101.custommail;

import emu.lunarcore.command.Command;
import emu.lunarcore.command.CommandArgs;
import emu.lunarcore.command.CommandHandler;
import emu.lunarcore.data.GameData;
import emu.lunarcore.data.excel.ItemExcel;
import emu.lunarcore.game.inventory.GameItem;
import emu.lunarcore.game.mail.Mail;
import emu.lunarcore.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Command(
        label = "custommail",
        desc = "/custommail [title] | [sender] | [message] $(item1) $(item2) $(item3). Send a custom mail to a player.",
        permission = "player.custommail",
        aliases = {"cmail"},
        requireTarget = true
)
public class CustomMailCommand implements CommandHandler {

    private static final String ATTACHMENT_PATTERN = " *\\$\\((.+?)\\) *";
    private static final String TARGET_PATTERN = " *@\\d+ *";

    @Override
    public void execute(CommandArgs args) {
        var target = args.getTarget();

        var targetMatcher = Pattern.compile(TARGET_PATTERN).matcher(args.getRaw());
        var params = targetMatcher.replaceFirst("").trim();
        var attachments = new LinkedList<GameItem>();

        var matcher = Pattern.compile(ATTACHMENT_PATTERN).matcher(params);
        params = matcher.replaceAll(matchResult -> {
            var attachment = matchResult.group(1);
            attachments.addAll(parseItem(attachment));
            return "";
        });

        var split = params.split("\\|", 3);
        var title = split[0].trim();
        var sender = split.length > 1 ? split[1].trim() : "";
        var message = split.length > 2 ? split[2].trim() : "";
        var mail = new Mail(title, sender, message);

        for (var attachment : attachments) {
            mail.addAttachment(attachment);
        }

        target.getMailbox().sendMail(mail);

        args.sendMessage("Mail sent.");
    }

    List<GameItem> parseItem(String itemText) {
        var args = new CommandArgs(null, Arrays.stream(itemText.split(" ")).collect(Collectors.toCollection(ArrayList::new)));

        // Setup items
        List<GameItem> items = new LinkedList<>();

        // Get amount to give
        int amount = Math.max(args.getAmount(), 1);

        // Parse items
        for (String arg : args.getList()) {
            // Parse item id
            int itemId = Utils.parseSafeInt(arg);

            ItemExcel itemData = GameData.getItemExcelMap().get(itemId);
            if (itemData == null) {
                args.sendMessage("Item \"" + arg + "\" does not exist!");
                continue;
            }

            // Add item
            GameItem item = new GameItem(itemData, amount);
            args.setProperties(item);

            items.add(item);
        }

        return items;
    }

}
