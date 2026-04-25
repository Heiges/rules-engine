import { useRuleset } from '../context/RulesetContext'
import './StatusBar.css'

export function StatusBar() {
  const { currentRuleset } = useRuleset()

  return (
    <div className="status-bar">
      {currentRuleset
        ? <>Aktuelles Regelwerk: <strong>{currentRuleset}</strong></>
        : 'Kein Regelwerk geladen'}
    </div>
  )
}
