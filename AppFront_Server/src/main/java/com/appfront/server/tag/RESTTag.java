package com.appfront.server.tag;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.appfront.server.repositories.TagRepository;
import com.appfront.server.resources.Tag;

/**
 * A REST-endpoint for tag querying
 * 
 * @author ente
 */
@RestController
@RequestMapping("/tags")
public class RESTTag {
    
    private static final ArrayList<Tag> INIT_TAGS      = new ArrayList<Tag>();
    /**
     * The sorted field.
     */
    private static final String         TAG_SORT_FIELD = "tag";
    @Autowired
    private TagRepository               tagRepository;
    
    /**
     * Get tags
     * 
     * @return a list of tags
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<TagList> getTags() {
        final Sort tagSort = new Sort(new Order(Direction.ASC, TAG_SORT_FIELD));
        final Iterable<Tag> tagsIterator = tagRepository.findAll(tagSort);
        final TagList tagsList = new TagList(tagsIterator);
        tagsList.add(linkTo(methodOn(RESTTag.class).getTags()).withSelfRel());
        return new ResponseEntity<TagList>(tagsList, HttpStatus.OK);
    }
    
    /**
     * Initialize some tags.
     */
    @RequestMapping(method = RequestMethod.PUT)
    public void initTags() {
        INIT_TAGS.clear();
        // Add a bunch of sample tags
        final Tag magic = new Tag();
        magic.setTag("Magic");
        INIT_TAGS.add(magic);
        final Tag pokemon = new Tag();
        pokemon.setTag("Pokemon");
        INIT_TAGS.add(pokemon);
        final Tag surfer = new Tag();
        surfer.setTag("Surfer");
        INIT_TAGS.add(surfer);
        // first delete all
        tagRepository.deleteAll();
        // then add all init tags again
        for (Tag tag : INIT_TAGS) {
            tagRepository.save(tag);
        }
    }
}
