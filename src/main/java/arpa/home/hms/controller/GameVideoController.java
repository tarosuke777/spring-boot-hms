package arpa.home.hms.controller;

import arpa.home.hms.form.GameVideoForm;
import arpa.home.hms.service.GameVideoService;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gameVideo")
public class GameVideoController {

  private static final String LIST_VIEW = "gameVideo/list";
  private static final String DETAIL_VIEW = "gameVideo/detail";

  private final GameVideoService gameVideoService;

  public GameVideoController(GameVideoService gameVideoService) {
    this.gameVideoService = gameVideoService;
  }

  @GetMapping("/list")
  public String getList(@PageableDefault(size = 10, sort = {"createdAt", "id"},
      direction = Direction.DESC) Pageable pageable, Model model) {
    Page<GameVideoForm> gameVideoPage =
        gameVideoService.getGameVideoPage(Objects.requireNonNull(pageable));
    model.addAttribute("gameVideoPage", gameVideoPage);
    return LIST_VIEW;
  }

  @GetMapping("/detail/{id}")
  public String getDetail(@PathVariable("id") Long id, Model model) {
    model.addAttribute("gameVideo", gameVideoService.getGameVideo(id));
    return DETAIL_VIEW;
  }
}
