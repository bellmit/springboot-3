package com.leo.boot.web.controller;

import com.leo.boot.web.vo.ParamVO;
import com.leo.boot.web.vo.ResultVO;
import io.swagger.annotations.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Api(tags = "Restful测试")
@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestfulController {

    @ApiOperation("查询")
    @GetMapping("/info/{param}")
    public ResultVO<ParamVO> get(@ApiParam(value = "path参数") @PathVariable String param,
                                 @ApiParam(value = "url参数") @Valid ParamVO paramVO) {
        return ResultVO.<ParamVO>success().setData(paramVO);
    }

    @ApiOperation("设置")
    @PostMapping("/info")
    public ResultVO<ParamVO> post(@ApiParam(value = "url参数") @RequestParam String param,
                                  @ApiParam(value = "body参数") @Valid @RequestBody ParamVO paramVO) {
        return ResultVO.<ParamVO>success().setData(paramVO);
    }

    @ApiOperation("分页")
    @GetMapping("/infos")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", type = "int", value = "当前页数", paramType = "query"),
            @ApiImplicitParam(name = "size", type = "int", value = "每页记录数", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序相关信息", paramType = "query", allowMultiple = true)})
    public ResultVO<List<String>> page(@ApiParam(value = "参数") @RequestParam String param,
                                       @PageableDefault(value = 20, sort = {"id"}, direction = Sort.Direction.DESC)
                                       @SortDefault.SortDefaults({
                                               @SortDefault(sort = "ordinal", direction = Sort.Direction.ASC),
                                               @SortDefault(sort = "publishTime", direction = Sort.Direction.DESC)
                                       }) Pageable pageable) {
        List<String> content = Arrays.asList("a", "b", "c");
        return ResultVO.<List<String>>success().setPage(new PageImpl<>(content, pageable, 100));
    }

}
