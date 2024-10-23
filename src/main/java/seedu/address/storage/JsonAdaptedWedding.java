package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Person;
import seedu.address.model.wedding.Datetime;
import seedu.address.model.wedding.Venue;
import seedu.address.model.wedding.Wedding;
import seedu.address.model.wedding.WeddingName;

/**
 * Jackson-friendly version of {@link Wedding}.
 */
public class JsonAdaptedWedding {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Wedding's %s field is missing!";

    private final String weddingName;
    private final String venue;
    private final String dateTime;
    private final List<JsonAdaptedPerson> participants = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedWedding} with the given wedding details.
     */
    @JsonCreator
    public JsonAdaptedWedding(@JsonProperty("weddingName") String weddingName,
            @JsonProperty("venue") String venue, @JsonProperty("dateTime") String dateTime,
            @JsonProperty("participants") List<JsonAdaptedPerson> participants) {
        this.weddingName = weddingName;
        this.venue = venue;
        this.dateTime = dateTime;
        if (participants != null) {
            this.participants.addAll(participants);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedWedding(Wedding source) {
        weddingName = source.getWeddingName().toString();
        venue = source.getVenue().toString();
        dateTime = source.getDatetime().toString();
        participants.addAll(source.getParticipants().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted wedding object into the model's {@code Wedding} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted wedding.
     */
    public Wedding toModelType() throws IllegalValueException {
        final List<Person> participantList = new ArrayList<>();
        for (JsonAdaptedPerson participant : participants) {
            participantList.add(participant.toModelType());
        }

        if (weddingName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    WeddingName.class.getSimpleName()));
        }
        if (!WeddingName.isValidWeddingName(weddingName)) {
            throw new IllegalValueException(WeddingName.MESSAGE_CONSTRAINTS);
        }
        final WeddingName modelWeddingName = new WeddingName(weddingName);

        if (venue == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Venue.class.getSimpleName()));
        }
        if (!Venue.isValidVenue(venue)) {
            throw new IllegalValueException(Venue.MESSAGE_CONSTRAINTS);
        }
        final Venue modelVenue = new Venue(venue);

        if (dateTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Datetime.class.getSimpleName()));
        }
        if (!Datetime.isValidDatetime(dateTime)) {
            throw new IllegalValueException(Datetime.MESSAGE_CONSTRAINTS);
        }
        final Datetime modelDatetime = new Datetime(dateTime);

        final Set<Person> modelParticpants = new HashSet<>(participantList);
        return new Wedding(modelWeddingName, modelVenue, modelDatetime, modelParticpants);
    }

}
