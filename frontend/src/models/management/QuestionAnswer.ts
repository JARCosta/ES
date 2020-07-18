import Question from '@/models/management/Question';
import Option from '@/models/management/Option';
import AnswerDetails from '@/models/management/questions/AnswerDetails';
import MultipleChoiceAnswerType from '@/models/management/questions/MultipleChoiceAnswerDetails';
import { createAnswerDetails } from '@/models/management/questions/Helpers';

export class QuestionAnswer {
  question!: Question;
  answerDetails: AnswerDetails = new MultipleChoiceAnswerType();

  constructor(jsonObj?: QuestionAnswer) {
    if (jsonObj) {
      this.question = new Question(jsonObj.question);
      this.answerDetails = createAnswerDetails(jsonObj);
    }
  }
}
