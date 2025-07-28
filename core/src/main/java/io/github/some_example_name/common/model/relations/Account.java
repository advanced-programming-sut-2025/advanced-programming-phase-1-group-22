package io.github.some_example_name.common.model.relations;

import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Account {
    private Integer id;
    private Integer golds = 0;
    private Integer earned = 0;

    public void removeGolds(int count) {
        setGolds(getGolds() - count);
    }

    public void setGolds(int count) {
        Player player = null;
        for (Player player1 : App.getInstance().getCurrentGame().getPlayers()) {
            if (player1.getAccount() == this) player = player1;
        }
        if (player != null) {
            if (player.getCouple() != null) GameClient.getInstance().setGolds(count, player.getCouple().getUser().getUsername());
            setGoldsByServer(count);
        }
    }

    public void setGoldsByServer(int count) {
        if (count > golds) earned += (count - golds);
        golds = count;
    }
}
